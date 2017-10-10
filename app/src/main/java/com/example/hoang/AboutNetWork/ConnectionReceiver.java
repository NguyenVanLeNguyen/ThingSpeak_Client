package com.example.hoang.AboutNetWork;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by nguyenhoang on 10/10/2017.
 */

public class ConnectionReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

    }

    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) WifiApp.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }
}
