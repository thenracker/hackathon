package cz.koci.hackathon.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.dropbox.core.android.Auth;

import butterknife.OnClick;
import cz.koci.hackathon.R;
import cz.koci.hackathon.dashboard.DashboardActivity;

/**
 * Created by petrw on 25.10.2017.
 */

public class LoginFragment extends DropboxFragment {

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_login;
    }

    @Override
    protected boolean loginWhenNoToken() {
        return false;
    }

    @OnClick(R.id.loginButton)
    public void onLoginClicked(View view) {
        Auth.startOAuth2Authentication(getContext(), getString(R.string.dropbox_app_key));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void onClientReady() {
        Intent intent = new Intent(getActivity(), DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}
