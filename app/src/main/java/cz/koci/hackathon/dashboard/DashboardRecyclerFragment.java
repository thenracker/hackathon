package cz.koci.hackathon.dashboard;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.github.clans.fab.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.core.v2.files.FileMetadata;
import com.github.clans.fab.FloatingActionMenu;

import java.text.DateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.koci.hackathon.R;
import cz.koci.hackathon.detail.DetailActivity;
import cz.koci.hackathon.login.DropboxFragment;
import cz.koci.hackathon.login.service.DropboxClientFactory;
import cz.koci.hackathon.model.Folder;
import cz.koci.hackathon.model.Metadata;
import cz.koci.hackathon.model.dto.ListFolderArgument;
import cz.koci.hackathon.service.RestClient;
import cz.koci.hackathon.service.UploadFileTask;
import cz.koci.hackathon.utils.FileUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by petrw on 25.10.2017.
 */

public class DashboardRecyclerFragment extends DropboxFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = DashboardRecyclerFragment.class.getSimpleName();

    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 0;
    private static final int PICKFILE_REQUEST_CODE = 99;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.menuFab)
    FloatingActionMenu menuFab;
    @BindView(R.id.openCameraFab)
    FloatingActionButton openCameraFab;
    @BindView(R.id.pickFileFab)
    FloatingActionButton pickFileFab;

    private DashboardRecyclerAdapter adapter;
    private String currentFolder = "";

    private static final String ARG_IS_SHARED = "ARG_IS_SHARED";
    private boolean isShared; //odeslané vs přijaté odkazy a soubory

    public static DashboardRecyclerFragment newInstance(boolean iShared) { //todo
        Bundle args = new Bundle();
        DashboardRecyclerFragment fragment = new DashboardRecyclerFragment();
        args.putBoolean(ARG_IS_SHARED, iShared);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onClientReady() {
        swipeRefreshLayout.setEnabled(true);
        onRefresh();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_dashboard_recycler;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            isShared = getArguments().getBoolean(ARG_IS_SHARED);
        }

        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setOnRefreshListener(this);
        if (!hasToken()) {
            swipeRefreshLayout.setEnabled(false);
            swipeRefreshLayout.setRefreshing(true);
        }

        adapter = new DashboardRecyclerAdapter();
        //adapter.setOurDB..
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        if (isShared) {
            menuFab.setVisibility(View.VISIBLE);
            pickFileFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkPermissionsAndUpload();
                }
            });
            openCameraFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo
                }
            });
        } else {
            pickFileFab.setVisibility(View.GONE);
        }

    }

    private void checkPermissionsAndUpload() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_REQUEST_CODE);
            }
        } else {
            FileUtils.pickFile(getActivity());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            FileUtils.pickFile(getActivity());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICKFILE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                uploadFile(data.getData().toString());
            }
        }
    }

    private void uploadFile(String fileUri) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage("Uploading");
        dialog.show();

        new UploadFileTask(getContext(), DropboxClientFactory.getClient(), new UploadFileTask.Callback() {
            @Override
            public void onUploadComplete(FileMetadata result) {
                dialog.dismiss();

                String message = result.getName() + " size " + result.getSize() + " modified " +
                        DateFormat.getDateTimeInstance().format(result.getClientModified());
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT)
                        .show();

                onRefresh();
            }

            @Override
            public void onError(Exception e) {
                dialog.dismiss();

                Log.e(TAG, "Failed to upload file.", e);
                Toast.makeText(getContext(),
                        "An error has occurred",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }).execute(fileUri, currentFolder);
    }

    @Override
    protected int getMenuResId() {
        return R.menu.dashboard_menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuRefresh) {
            swipeRefreshLayout.setRefreshing(true);
            onRefresh();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        ListFolderArgument arg = new ListFolderArgument();
        arg.setPath("");
        RestClient.get().listFolder(arg).enqueue(new Callback<Folder>() {
            @Override
            public void onResponse(Call<Folder> call, Response<Folder> response) {
                if (response.code() == 200) {
                    adapter.setEntries(response.body().getEntries());
                    adapter.notifyDataSetChanged();
                } else if (response.code() >= 400) {
                    // TODO: 25.10.2017
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Folder> call, Throwable t) {

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        swipeRefreshLayout.setRefreshing(false);
    }

    class DashboardRecyclerAdapter extends RecyclerView.Adapter<DashboardRecyclerAdapter.ViewHolder> {

        private List<Metadata> entries;

        public DashboardRecyclerAdapter() {

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_dashboard, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Metadata metadata = entries.get(position);
            if (metadata.getType().equals(Metadata.Type.FOLDER)) {
                holder.imageView.setImageResource(R.drawable.ic_folder_white_circle_green_24px);
            } else {
                if (metadata.getName().contains(".pdf")) {
                    holder.imageView.setImageResource(R.drawable.ic_pdf_white_circle_green_24px);
                } else {
                    holder.imageView.setImageResource(R.drawable.ic_file_white_circle_green_24px);
                }
            }
            holder.nameTextView.setText(entries.get(position).getName());
            holder.subNameTextView.setText(entries.get(position).getTag());
        }

        @Override
        public int getItemCount() {
            return entries == null ? 0 : entries.size();
        }

        public void setEntries(List<Metadata> entries) {
            this.entries = entries;
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            @BindView(R.id.nameTextView)
            protected TextView nameTextView;
            @BindView(R.id.subNameTextView)
            protected TextView subNameTextView;
            @BindView(R.id.imageView)
            protected ImageView imageView;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), DetailActivity.class);
                startActivity(i);
            }
        }
    }
}
