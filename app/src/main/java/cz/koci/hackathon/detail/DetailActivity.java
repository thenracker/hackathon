package cz.koci.hackathon.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;

import cz.koci.hackathon.R;
import cz.koci.hackathon.shared.BaseActivity;


/**
 * Created by Matej Danicek on 25.10.17.
 */

public class DetailActivity extends BaseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, DetailFragment.newInstance()).commit();
        }
    }
}
