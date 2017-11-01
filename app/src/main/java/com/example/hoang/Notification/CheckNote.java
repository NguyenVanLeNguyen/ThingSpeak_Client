package com.example.hoang.Notification;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoang.app.MainActivity;
import com.example.hoang.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckNote extends JobService {


    public CheckNote() {
        super();
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d("TAG","onStart");

        /*CheckServer check = new CheckServer();
        check.execute(jobParameters);*/
        //


                //getDecorView().findViewById(android.R.id.content);
        //LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //assert inflater != null;
        //@SuppressLint("InflateParams")
       // View mv = inflater.inflate(R.layout.item,null,true);


        //t.setText("API chanel");
       /* ListView lv = mv.findViewById(R.id.liv1);
        CustomAdapter cus  = (CustomAdapter) lv.getAdapter();
        GregorianCalendar now = new GregorianCalendar();
        Random rand = new Random();
        double value = rand.nextInt(100);

        cus.getObjects().get(0).setLastEntryTime(now);
        cus.getObjects().get(0).setLastEntryValue(value);
        cus.notifyDataSetChanged();*/
        Toast.makeText(getApplicationContext(),"change:",Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d("TAG","onStop");

        return false;
    }

    @SuppressLint("StaticFieldLeak")
    private class CheckServer extends AsyncTask<JobParameters,Void,JobParameters> {

        private Notification.Builder notBuilder ;

        private   int MY_NOTIFICATION_ID = 16543;

        private static final int MY_REQUEST_CODE = 100;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JobParameters doInBackground(JobParameters... parameters) {
            String APIkey = parameters[0].getExtras().getString("abc");
            String dataJson = getJsonChanelfromUrl(APIkey);
            try {
                JSONObject jsonObject = new JSONObject(dataJson);
                int leng = jsonObject.length();
                for(int i = 3; i<= leng;i++ ){
                    if(!jsonObject.isNull("field" + (i-2))){
                        double val = jsonObject.getDouble("field" + (i-2));

                        if(val <= 40.0  ){
                            Log.d("result","Dangerous" + APIkey);
                            raiseNotification();
                            return  parameters[0];
                        }
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("result","safe");
            return parameters[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            super.onPostExecute(jobParameters);
            jobFinished(jobParameters, false);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        private String getJsonChanelfromUrl(String chanelID)
        {
            InputStream inputStream = null;
            BufferedReader buff = null;
            String address = "https://api.thingspeak.com/channels/" + chanelID +"/feeds/last.json";
            StringBuilder result =  new StringBuilder();
            //get json from sever (chanel's information)
            try{
                URL url = new URL(address);
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                inputStream = url.openStream();
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                int resCode = httpConn.getResponseCode();
                if(resCode == HttpURLConnection.HTTP_OK){
                    inputStream = httpConn.getInputStream();
                    buff = new BufferedReader(new InputStreamReader(inputStream));
                    String line ;
                    while((line = buff.readLine()) != null){
                        result.append(line);
                        result.append("\n");
                    }
                    Log.d("InputStream", "ok");
                    return result.toString();
                }
                else{

                    return "erron";
                }
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    if(inputStream != null && buff != null){
                        inputStream.close();
                        buff.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return "erron";
        }

        private void raiseNotification(){


            this.notBuilder = new Notification.Builder(getApplicationContext());
            this.notBuilder.setAutoCancel(true);
            this.notBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
            this.notBuilder.setTicker("This is a ticker");

            this.notBuilder.setWhen(System.currentTimeMillis()+ 10* 1000);
            this.notBuilder.setContentTitle("Warning");
            this.notBuilder.setContentText(" Oil tanks are becoming exhaust");

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), MY_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);


             this.notBuilder.setContentIntent(pendingIntent);

            // Lấy ra dịch vụ thông báo (Một dịch vụ có sẵn của hệ thống).
            NotificationManager notificationService  =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Xây dựng thông báo và gửi nó lên hệ thống.

            Notification notification =  notBuilder.build();
            assert notificationService != null;
            notificationService.notify(MY_NOTIFICATION_ID, notification);

        }
    }
}
