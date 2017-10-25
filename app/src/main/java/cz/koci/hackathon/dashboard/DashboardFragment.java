package cz.koci.hackathon.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.koci.hackathon.R;
import cz.koci.hackathon.login.DropboxFragment;
import cz.koci.hackathon.model.Folder;
import cz.koci.hackathon.model.dto.ListFolderArgument;
import cz.koci.hackathon.service.RestClient;
import cz.koci.hackathon.shared.BaseActivity;
import cz.koci.hackathon.shared.BaseFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by petrw on 25.10.2017.
 */

public class DashboardFragment extends DropboxFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    DashboardRecyclerAdapter adapter;

    public static DashboardFragment newInstance() {
        Bundle args = new Bundle();
        DashboardFragment fragment = new DashboardFragment();
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
        return R.layout.fragment_dashboard;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseActivity activity = (BaseActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(R.string.dashboard_title);

        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setOnRefreshListener(this);
        if (!hasToken()) {
            swipeRefreshLayout.setEnabled(false);
        }

        adapter = new DashboardRecyclerAdapter();
        //adapter.setOurDB..
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected int getMenuResId() {
        return R.menu.dashboard_menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuRefresh){
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
                if (response.code() == 200){
                    System.out.println("tt");
                } else if (response.code() >= 400){

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

        public DashboardRecyclerAdapter() {
            //todo
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_dashboard, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.nameTextView.setText("Test " + position);
            holder.subNameTextView.setText("SubTest " + position);
        }

        @Override
        public int getItemCount() {
            return 50;
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            @BindView(R.id.nameTextView)
            protected TextView nameTextView;
            @BindView(R.id.subNameTextView)
            protected TextView subNameTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
            }
        }
    }
}
