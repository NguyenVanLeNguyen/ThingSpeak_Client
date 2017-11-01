package com.example.hoang.app;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by hoang on 22/10/2017.
 */

public class GetFieldFeed extends AsyncTask<Device,String,Device> {
    private ProgressDialog progressDialog;
    private ShowListDevice activity;
    private ProcessingTime convertTime;
    private DateFormat formatTimeJson = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");


    public GetFieldFeed(ShowListDevice activity) {
        this.activity = activity;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        convertTime = new ProcessingTime();
        convertTime.setFormat(formatTimeJson);
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Please Wait!");
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Device devi) {
        progressDialog.dismiss();
        super.onPostExecute(devi);
        activity.provideGraph(devi);

    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Device doInBackground(Device... devi) {
        devi[0].setData(getFieldFeed(devi[0]));
        return devi[0];
    }

    public ArrayList<Pair<GregorianCalendar,Double>> getFieldFeed(Device devi){
        InputStream inputStream = null;
        BufferedReader buff = null;
        ArrayList<Pair<GregorianCalendar,Double>> feeds = new ArrayList<>();
        String APIchanel = devi.getAPIchanel();
        String id = devi.getId();
       Long lastEntryTimeMilis = devi.getLastEntryTime().getTimeInMillis();
        GregorianCalendar landmarkSevenDayAgo = new GregorianCalendar();
        landmarkSevenDayAgo.setTimeInMillis(lastEntryTimeMilis - 518400000);
        landmarkSevenDayAgo.set(Calendar.HOUR,0);
        landmarkSevenDayAgo.set(Calendar.MINUTE,0);
        landmarkSevenDayAgo.set(Calendar.SECOND,0);
        Date date = landmarkSevenDayAgo.getTime();
        landmarkSevenDayAgo = null;
        String landmarkToString = formatTimeJson.format(date);
        String address = "https://api.thingspeak.com/channels/" + APIchanel + "/fields/" + id +".json?start=" + landmarkToString ;
       // Log.d("url",address);
        StringBuilder result =  new StringBuilder();
        int hour = 0;
        int dateofmonth = 0;
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
            }
            else{
                Log.d("InputStream", "erron");
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

        try{
            JSONObject jsonRoot = new JSONObject(result.toString());
            JSONArray data = jsonRoot.getJSONArray("feeds");


            for(int i = (data.length()-1); i >= 0; i-- ){
                JSONObject jObject = data.getJSONObject(i);
                GregorianCalendar time = convertTime.getTime(jObject.getString("created_at"));
                int h = time.get(Calendar.HOUR_OF_DAY);
                int d = time.get(Calendar.DATE);
                if((h != hour) || (d != dateofmonth)){
                    if(jObject.isNull("field" + id)) {
                      //  Log.d("status: ","null");

                    }
                    else {
                        double value = jObject.getDouble("field" + id);
                        feeds.add(0,Pair.create(time,value));
                        //Log.d("hour:", time.get(Calendar.HOUR_OF_DAY)+"");
                        //Log.d("date:", time.get(Calendar.DATE)+"");
                    }

                    hour = h;
                    dateofmonth = d;
                }



            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return feeds;
    }


}
