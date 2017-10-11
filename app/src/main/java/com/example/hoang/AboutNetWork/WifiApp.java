package com.example.hoang.AboutNetWork;

import android.app.Application;

/**
<<<<<<< HEAD
 * Created by nguyenhoang on 10/10/2017.
=======
 * Created by hoang on 11/10/2017.
>>>>>>> 6399522a9c2a13883065c707c37e324ce65b53b6
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
