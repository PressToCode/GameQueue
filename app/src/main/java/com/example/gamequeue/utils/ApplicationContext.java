package com.example.gamequeue.utils;

import android.app.Application;
import android.content.Context;

public class ApplicationContext extends Application {
    private static ApplicationContext instance;
    private static Boolean devMode = false;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this; // store the app-wide context
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }

    // TODO: Remove on Production
    // TODO: Remove the layout for Dev Mode on Production
    public static void setDevMode(Boolean val) { devMode = val; }
    public static Boolean getDevMode() { return devMode; }
}
