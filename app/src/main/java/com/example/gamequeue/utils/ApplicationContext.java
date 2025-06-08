package com.example.gamequeue.utils;

import android.app.Application;
import android.content.Context;

public class ApplicationContext extends Application {
    private static ApplicationContext instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this; // store the app-wide context
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}
