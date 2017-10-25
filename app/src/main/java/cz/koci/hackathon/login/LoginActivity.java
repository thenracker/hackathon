package cz.koci.hackathon.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import cz.koci.hackathon.R;
import cz.koci.hackathon.model.datasource.ReceivedLinkDataSource;
import cz.koci.hackathon.model.table.ReceivedLink;
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

        Intent intent = getIntent();
        String action = intent.getAction();
        if (action != null && action.equals(Intent.ACTION_VIEW)) {
            Uri uri = intent.getData();
            String scheme = uri.getScheme();

            if (scheme.equals("http")) {
                String link = uri.getQueryParameter("link");
                ReceivedLinkDataSource.createOrUpdate(new ReceivedLink(link));
            }
        }

    }

}
