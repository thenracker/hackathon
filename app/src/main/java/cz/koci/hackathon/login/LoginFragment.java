package cz.koci.hackathon.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.koci.hackathon.R;
import cz.koci.hackathon.shared.BaseFragment;

/**
 * Created by petrw on 25.10.2017.
 */

public class LoginFragment extends BaseFragment {

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

    @OnClick(R.id.loginButton)
    public void onLoginClicked(View view) {
        Toast.makeText(getActivity(), "login", Toast.LENGTH_SHORT).show();
    }
}
