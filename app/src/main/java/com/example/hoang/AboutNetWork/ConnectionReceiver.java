package com.example.hoang.AboutNetWork;

<<<<<<< HEAD

=======
>>>>>>> 6399522a9c2a13883065c707c37e324ce65b53b6
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
<<<<<<< HEAD

/**
 * Created by nguyenhoang on 10/10/2017.
 */

public class ConnectionReceiver extends BroadcastReceiver{
=======
/**
 * Created by hoang on 11/10/2017.
 */

public class ConnectionReceiver extends BroadcastReceiver {
>>>>>>> 6399522a9c2a13883065c707c37e324ce65b53b6
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
<<<<<<< HEAD
=======

>>>>>>> 6399522a9c2a13883065c707c37e324ce65b53b6
}
