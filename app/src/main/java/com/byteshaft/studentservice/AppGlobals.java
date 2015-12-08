package com.byteshaft.studentservice;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class AppGlobals extends Application {

    private static Context sContext;
    private static SharedPreferences sPreferences;
    private static final String VIRGIN_KEY = "virgin";
    private static final String USER_NAME = "user_name";

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }
    public static Context getContext() {
        return sContext;
    }

    public static boolean isVirgin() {
        return sPreferences.getBoolean(VIRGIN_KEY, true);
    }

    public static void setVirgin(boolean virgin) {
        sPreferences.edit().putBoolean(VIRGIN_KEY, virgin).apply();
    }

    public static void putUsername(String username) {
        sPreferences.edit().putString(USER_NAME, username).apply();
    }
}
