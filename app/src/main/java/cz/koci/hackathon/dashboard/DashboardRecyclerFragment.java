package cz.koci.hackathon.dashboard;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import com.dropbox.core.v2.files.FileMetadata;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.koci.hackathon.R;
import cz.koci.hackathon.dialog.ShareDialogFragment;
import cz.koci.hackathon.login.DropboxFragment;
import cz.koci.hackathon.login.service.DropboxClientFactory;
import cz.koci.hackathon.model.Folder;
import cz.koci.hackathon.model.Metadata;
import cz.koci.hackathon.model.dto.ListFolderArgument;
import cz.koci.hackathon.service.RestClient;
import cz.koci.hackathon.service.UploadFileTask;
import cz.koci.hackathon.shared.LinkMetadataLoadedEvent;
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
    private static final int CAMERA_REQUEST_CODE = 2;
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

    @BindView(R.id.emptyView)
    View emptyView;

    private DashboardRecyclerAdapter adapter;
    private String currentFolder = "";

    public static final String ARG_IS_SHARED = "ARG_IS_SHARED";
    public static final String ARG_ROOT_PATH = "ARG_ROOT_PATH";

    private boolean myFiles; //odeslané vs přijaté odkazy a soubory

    public static DashboardRecyclerFragment newInstance(String rootPath, boolean iShared) { //todo
        Bundle args = new Bundle();
        DashboardRecyclerFragment fragment = new DashboardRecyclerFragment();
        args.putBoolean(ARG_IS_SHARED, iShared);
        args.putString(ARG_ROOT_PATH, rootPath);
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
        EventBus.getDefault().register(this);

        if (getArguments() != null) {
            myFiles = getArguments().getBoolean(ARG_IS_SHARED);
            currentFolder = getArguments().getString(ARG_ROOT_PATH);
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

        if (myFiles) {
            menuFab.setVisibility(View.VISIBLE);
            pickFileFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkPermissionsAndUpload();
                    menuFab.close(true);
                }
            });
            openCameraFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkPermissionsAndSnapshot();
                    menuFab.close(true);
                }
            });
        } else {
            menuFab.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void checkPermissionsAndUpload() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                FileUtils.pickFile(this);
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_REQUEST_CODE);
            }
        } else {
            FileUtils.pickFile(this);
        }
    }

    private void checkPermissionsAndSnapshot() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
            } else {
                takePicture();
            }
        } else {
            FileUtils.pickFile(this);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            FileUtils.pickFile(this);
        } else {
            takePicture();
        }
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Uri uri = Uri.parse("file:///sdcard/photo.jpg");
        //intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICKFILE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                uploadFile(data.getData().toString());
            }
        }

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            //todo

        }
    }

    private void uploadFile(String fileUri) {

        buildNotification(getString(R.string.sending), getString(R.string.sending_in_progress), true);

        new UploadFileTask(getContext(), DropboxClientFactory.getClient(), new UploadFileTask.Callback() {
            @Override
            public void onUploadComplete(FileMetadata result) {
                buildNotification(getString(R.string.sending), getString(R.string.sending_finished), false);
                onRefresh();
            }

            @Override
            public void onError(Exception e) {
                buildNotification(getString(R.string.sending), getString(R.string.sending_failed), false);
            }
        }).execute(fileUri, currentFolder);
    }

    private void buildNotification(String title, String content, boolean ongoing) {
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, new Intent(), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentText(content);

        if (ongoing) {
            builder.setProgress(0, 0, true);
        }

        Notification notification = builder.build();

        NotificationManagerCompat.from(getContext()).notify(TAG, 99, notification);
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
        arg.setPath(currentFolder);
        if (myFiles) {
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
        } else {
            List<Metadata> metadatas = SQLite.select().from(Metadata.class).queryList();
            adapter.setEntries(metadatas);
            adapter.notifyDataSetChanged();
//            arg.setPath(""); //TODO - od matěje z DB flow - pro každý soubor
//            RestClient.get().getListSharedLinkMetadata(arg).enqueue(new Callback<Folder>() {
//                @Override
//                public void onResponse(Call<Folder> call, Response<Folder> response) {
//                    if (response.code() == 200) {
//                        System.out.println("kok");
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Folder> call, Throwable t) {
//                    System.out.println("kook2");
//                }
//            });
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMetadataLoaded(LinkMetadataLoadedEvent event) {
        if (!myFiles) {
            List<Metadata> metadatas = SQLite.select().from(Metadata.class).queryList();
            adapter.setEntries(metadatas);
            adapter.notifyDataSetChanged();
        }

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
                } else if (metadata.getName().contains(".zip")) {
                    holder.imageView.setImageResource(R.drawable.ic_zip_white_circle_green_24px);
                } else {
                    holder.imageView.setImageResource(R.drawable.ic_file_white_circle_green_24px);
                }
            }
            holder.nameTextView.setText(entries.get(position).getName());
            holder.subNameTextView.setText(entries.get(position).getTag());
            holder.thirdNameTextView.setText(R.string.not_shared_yet);

            if (position == entries.size() - 1) {
                holder.divider.setVisibility(View.GONE);
            } else {
                holder.divider.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public int getItemCount() {
            return entries == null ? 0 : entries.size();
        }

        public void setEntries(List<Metadata> entries) {
            this.entries = entries;
            emptyView.setVisibility(entries.isEmpty() ? View.VISIBLE : View.GONE);
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            @BindView(R.id.nameTextView)
            protected TextView nameTextView;
            @BindView(R.id.subNameTextView)
            protected TextView subNameTextView;
            @BindView(R.id.imageView)
            protected ImageView imageView;
            @BindView(R.id.thirdNameTextView)
            protected TextView thirdNameTextView;
            @BindView(R.id.menuImageView)
            protected ImageView menuImageView;
            @BindView(R.id.divider)
            View divider;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(this);
                menuImageView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if (v instanceof ImageView) {
                    showPopupMenu(getContext(), v, R.menu.metadata_item_menu, new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.menuShare) {
                                int position = getLayoutPosition();
                                Metadata entry = entries.get(position); //self yourself Dane

                                ShareDialogFragment dialogFragment = ShareDialogFragment.newInstance(entry.getId(), entry.getPathLower());
                                dialogFragment.show(getActivity().getSupportFragmentManager(), "ShareDialogFragment");
                            }
                            if (item.getItemId() == R.id.menuShowSharing) {

                            }
                            return true;
                        }
                    });
                } else {
                    final Metadata metadata = entries.get(getAdapterPosition());
                    if (metadata.getType() == Metadata.Type.FOLDER) {
                        Intent i = new Intent(getActivity(), DashboardRecyclerActivity.class);
                        i.putExtra(ARG_ROOT_PATH, metadata.getPathLower());
                        i.putExtra(ARG_IS_SHARED, myFiles);
                        startActivity(i);
                    } else if (metadata.isDownloaded()) {
                        Intent intent = new Intent();
                        intent.setAction(android.content.Intent.ACTION_VIEW);
                        File file = new File(metadata.getLocalPath());

                        MimeTypeMap mime = MimeTypeMap.getSingleton();
                        String ext = file.getName().substring(file.getName().indexOf(".") + 1);
                        String type = mime.getMimeTypeFromExtension(ext);

                        intent.setDataAndType(Uri.fromFile(file), type);

                        getContext().startActivity(intent);
                    } else {
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                buildNotification(getString(R.string.downloading), getString(R.string.downloading_in_progress), true);
                                try {
                                    downloadFile(metadata);
                                    metadata.setDownloaded(true);
                                    metadata.save();
                                    buildNotification(getString(R.string.downloading), getString(R.string.downloading_successful), false);
                                } catch (IOException e) {
                                    buildNotification(getString(R.string.downloading), getString(R.string.downloading_failed), false);
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    private void downloadFile(Metadata metadata) throws IOException {
        URL url;
        url = new URL(metadata.getUrl());
        URLConnection urlConnection = url.openConnection();
        urlConnection.connect();
        int fileSize = urlConnection.getContentLength();
        InputStream inputStream = urlConnection.getInputStream();
        String filePath = Environment.getExternalStorageDirectory() + "/Hackathon/" + metadata.getName();
        new File(filePath).getParentFile().mkdirs(); //vytvoření složek
        FileOutputStream fos = new FileOutputStream(filePath);

        int bytesRead;
        byte[] buffer = new byte[2048];
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            fos.write(buffer, 0, bytesRead);
        }

        fos.close();
        inputStream.close();
    }

    public static void showPopupMenu(final Context context, View v, int resource, PopupMenu.OnMenuItemClickListener listener) {
        PopupMenu popupMenu = new PopupMenu(context, v);
        popupMenu.getMenuInflater().inflate(resource, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(listener);
        popupMenu.show();
    }
}
