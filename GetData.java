package com.example.hoang.datafromserver;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
 * Created by hoang on 10/09/2017.
 */

public class GetData extends AsyncTask<Bundle,String,String> {
    private Activity mainactivity;
    private Bundle bundle;

    public GetData(Activity m){
        mainactivity = m;
    }

    @Override
    protected  void onPreExecute(){

    }

    @Override
    protected String doInBackground(Bundle... bun) {
        String content;
        bundle = bun[0];
        String value = "";
        content = getJsonfromUrl(bun[0]);
        String part = bun[0].getString(ShowInformationActivity.PART);

        try {
            JSONObject jobjRoot = new JSONObject(content);
            JSONArray jarray = jobjRoot.getJSONArray("feeds");
            value = jarray.getJSONObject(0).optString("part").toString();


        } catch (JSONException e){
            e.printStackTrace();
        }
        publishProgress(value);
        return null;
    }

    @Override
    protected void onPostExecute(String result){

    }

    @Override
    protected void onProgressUpdate(String... values) {
        TextView te;
        if(bundle.getString(ShowInformationActivity.PART).equals("field1"))
            te = (TextView) mainactivity.findViewById(R.id.tvw_Tem);
        else
            te = (TextView) mainactivity.findViewById(R.id.tvw_Hum);

        te.setText(values[0]);
    }

    public String getJsonfromUrl(Bundle bun){
        InputStream inputStream = null;
        InputStreamReader inputStreamreader = null;
        BufferedReader buff = null;
        String apikey = bun.getString(ShowInformationActivity.APIKEY);
        String address = "https://api.thingspeak.com/channels/313786/fields/1.json?api_key=" + apikey +"&results=1";
        StringBuilder result =  new StringBuilder();
        try{
            URL url = new URL(address);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);inputStream = url.openStream();
            httpConn.setRequestMethod("GET");inputStreamreader = new InputStreamReader(inputStream);
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
                Log.d("InputStream", "enrro");

                return null;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
                buff.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;

    }
}
