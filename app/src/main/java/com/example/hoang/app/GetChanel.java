package com.example.hoang.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hoang on 16/10/2017.
 */

public class GetChanel extends AsyncTask<String,String,Chanel> {
    private Activity mainactivity;
    private Bundle bundle;

    public GetChanel(Activity m){
        mainactivity = m;
    }

    @Override
    protected  void onPreExecute()
    {

    }

    @Override
    protected Chanel doInBackground(String... bun)
    {
        String content;
        Chanel chanel = new Chanel();
        String value = "";
        content = getJsonChanelfromUrl(bun[0]);
        if(!content.equals("erron"))
        {
            try
            {
                JSONObject jsonRoot = new JSONObject(content);
                JSONArray fields = jsonRoot.getJSONArray("tags");
                String id = jsonRoot.getString("id");
                String name = jsonRoot.getString("name");
                Double longitude = jsonRoot.getDouble("longitude");
                Double latitude = jsonRoot.getDouble("latitude");
                chanel.setAPIkey(id);
                chanel.setName(name);
                chanel.setLatitude(latitude);
                chanel.setLongitude(longitude);

            } catch (JSONException e)
            {
                e.printStackTrace();
            }

        }


        if(content != null)
        {
            publishProgress(content);
            Log.d("InputStream", "ok");
        }

        else
        {
            publishProgress(null);
            Log.d("InputStream", "erron");
        }

        return chanel;
    }

    @Override
    protected void onPostExecute(Chanel result)
    {

    }

    @Override
    protected void onProgressUpdate(String... values)
    {

    }

    private String getJsonChanelfromUrl(String chanelID)
    {
        InputStream inputStream = null;
        BufferedReader buff = null;
        String address = "https://api.thingspeak.com/channels/" + chanelID +".json";
        StringBuilder result =  new StringBuilder();
        try{
            URL url = new URL(address);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);inputStream = url.openStream();
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            int resCode = httpConn.getResponseCode();
            if(resCode == HttpURLConnection.HTTP_OK){
                inputStream = httpConn.getInputStream();
                buff = new BufferedReader(new InputStreamReader(inputStream));
                String line = "" ;
                while((line = buff.readLine()) != null){
                    result.append(line);
                    result.append("\n");
                }
                return result.toString();
            }
            else{
                Log.d("InputStream", "erron");

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




}
