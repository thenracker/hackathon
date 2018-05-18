package cz.koci.hackathon.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.koci.hackathon.R;
import cz.koci.hackathon.shared.BaseActivity;
import cz.koci.hackathon.shared.BaseFragment;

/**
 * Created by petrw on 25.10.2017.
 */

public class DetailFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    public static DetailFragment newInstance() {
        Bundle args = new Bundle();
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_detail;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BaseActivity activity = ((BaseActivity) getActivity());
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(R.string.login_title);
    }

}
