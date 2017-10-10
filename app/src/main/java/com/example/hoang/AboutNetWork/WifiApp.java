package com.example.hoang.AboutNetWork;

import android.app.Application;

/**
 * Created by nguyenhoang on 10/10/2017.
 */

public class WifiApp extends Application {
    static WifiApp wifiInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        wifiInstance = this;
    }

    public static synchronized WifiApp getInstance() {
        return wifiInstance;
    }
}
