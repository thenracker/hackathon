package cz.koci.hackathon.dashboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import cz.koci.hackathon.R;

/**
 * Created by vlado on 25/10/2017.
 */

public class DashboardRecyclerActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            String url = getIntent().getStringExtra(DashboardRecyclerFragment.ARG_ROOT_PATH);
            boolean isShared = getIntent().getBooleanExtra(DashboardRecyclerFragment.ARG_IS_SHARED, false);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, DashboardRecyclerFragment.newInstance(url, isShared))
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
