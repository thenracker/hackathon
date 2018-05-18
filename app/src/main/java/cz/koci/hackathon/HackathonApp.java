package cz.koci.hackathon;

import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;


/**
 * Created by petrw on 25.10.2017.
 */

public class HackathonApp extends MultiDexApplication {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        context = this;
        FlowManager.init(new FlowConfig.Builder(this).openDatabasesOnInit(true).build());
    }

    public static Context getContext() {
        return context;
    }
}
