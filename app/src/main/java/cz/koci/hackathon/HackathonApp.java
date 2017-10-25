package cz.koci.hackathon;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

/**
 * Created by petrw on 25.10.2017.
 */

public class HackathonApp extends MultiDexApplication {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
