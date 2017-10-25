package cz.koci.hackathon.utils;

import android.content.Context;
import android.content.SharedPreferences;
import cz.koci.hackathon.HackathonApp;


public class PrefManager {

    private static final String PREFS = "hackPref";
    private static final String TOKEN = "token";
    private static final String TOKEN_TIMESTAMP = "token_timestamp";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    public static String getToken() {
        return HackathonApp.getContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getString(TOKEN, "");
    }

    public static void setToken(String token) {
        SharedPreferences.Editor editor = HackathonApp.getContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit();
        editor.putString(TOKEN, token);
        editor.apply();
    }

}
