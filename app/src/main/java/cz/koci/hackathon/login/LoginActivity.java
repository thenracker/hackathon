package cz.koci.hackathon.login;

import android.os.Bundle;

import cz.koci.hackathon.R;
import cz.koci.hackathon.shared.BaseActivity;

public class LoginActivity extends BaseActivity {

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, LoginFragment.newInstance()).commit();
        }

    }

}
