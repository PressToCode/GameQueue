package com.example.gamequeue.utils;

import android.app.Application;
import android.content.Context;

public class ApplicationContext extends Application {
    private static ApplicationContext instance;
//    private static Boolean devMode;
    private static Boolean adminMode;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this; // store the app-wide context
//        devMode = false;
        adminMode = false;
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }

//    public static void setDevMode(Boolean val) { devMode = val; }
//    public static Boolean getDevMode() { return devMode; }

    // Toggle Admin Mode
    public static void setAdminMode(Boolean val) { adminMode = val; }
    public static Boolean getAdminMode() { return adminMode; }
}
