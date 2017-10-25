package cz.koci.hackathon.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import cz.koci.hackathon.HackathonApp;


public class PrefManager {

    private static final String PREFS = "hackPref";
    private static final String TOKEN = "token";
    private static final String TOKEN_TIMESTAMP = "token_timestamp";
    private static final String USER_ID = "user_id";

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Nullable
    public static String getToken() {
        return HackathonApp.getContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getString(TOKEN, null);
    }

    public static void setToken(String token) {
        SharedPreferences.Editor editor = HackathonApp.getContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit();
        editor.putString(TOKEN, token);
        editor.commit();
    }


    @Nullable
    public static String getUserId() {
        return HackathonApp.getContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getString(USER_ID, null);
    }

    public static void setUserId(String userId) {
        SharedPreferences.Editor editor = HackathonApp.getContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit();
        editor.putString(USER_ID, userId);
        editor.apply();
    }

}
