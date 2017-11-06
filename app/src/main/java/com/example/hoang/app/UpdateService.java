package com.example.hoang.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.hoang.AboutNetWork.ConnectionReceiver;

/**
 * Created by hoang on 05/11/2017.
 */

public class UpdateService extends Service {
    public static MainActivity mainActivity;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Toast.makeText(getApplicationContext(),"this is service",Toast.LENGTH_SHORT).show();
        return null;
    }

    /**
     * @param intent
     * @param startId
     * @deprecated
     */
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Toast.makeText(this, "ServiceClass.onStart()", Toast.LENGTH_LONG).show();
        Log.d("Testing", "Service got started");
        if(intent != null && ConnectionReceiver.isConnected()){
            String chanelID = intent.getStringExtra("abc");
            GetChanel getch = new GetChanel(mainActivity,1);
            getch.execute(chanelID);
        }

    }

}
