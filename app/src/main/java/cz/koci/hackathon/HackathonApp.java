package cz.koci.hackathon;

import android.app.Application;
import android.content.Context;

/**
 * Created by petrw on 25.10.2017.
 */

public class HackathonApp extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
