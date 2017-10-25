package cz.koci.hackathon.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cz.koci.hackathon.R;
import cz.koci.hackathon.login.LoginFragment;
import cz.koci.hackathon.model.datasource.ReceivedLinkDataSource;
import cz.koci.hackathon.model.table.ReceivedLink;
import cz.koci.hackathon.shared.BaseActivity;

/**
 * Created by petrw on 25.10.2017.
 */

public class DashboardActivity extends BaseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, DashboardFragment.newInstance()).commit();
        }

        for (ReceivedLink receivedLink : ReceivedLinkDataSource.findNewlyReceivedLinks()) {

        }
    }
}
