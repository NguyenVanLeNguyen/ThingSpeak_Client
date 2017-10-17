package com.example.hoang.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.example.hoang.app.R.layout.item;

/**
 * Created by hoang on 16/10/2017.
 */

public class GetChanel extends AsyncTask<String,String,Chanel>
{
    ProgressDialog progressDialog;
    private MainActivity mainactivity;
    private ProcessingTime convertTime;
    private DateFormat formatTimeJson = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    public GetChanel(MainActivity m){
        mainactivity = m;
    }

    @Override
    protected  void onPreExecute()
    {
        convertTime = new ProcessingTime();
        convertTime.setFormat(formatTimeJson);
        progressDialog = new ProgressDialog(mainactivity);
        progressDialog.setTitle("Please Wait!");
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Chanel doInBackground(String... bun)
    {
        String content;
        Chanel chanel = new Chanel();
        chanel.setStatus(checkStatusGateway(bun[0]));
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

                for(int i = 0; i < fields.length(); i++ ){
                    int j = i+1;
                    String nameDevi = fields.getJSONObject(i).getString("name");
                    //String nameDevi = fields.getJSONObject(i).getString(field);

                    Device device = creatDevice(nameDevi,id,j);
                    chanel.getFields().add(device);
                }

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
        progressDialog.dismiss();
        mainactivity.getListDevice().addAll(result.getFields());
        mainactivity.setList();
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

    //Check gateway's status
    private int checkStatusGateway(String chanelID)
    {
        int timeRequireFeback = 300;
        int ageOfLastEntry = 301 ;
        InputStream inputStream = null;
        BufferedReader buff = null;
        String address = "https://api.thingspeak.com/channels/" + chanelID +"/feeds/last_data_age.json";
        StringBuilder result =  new StringBuilder();
        //get json from sever (last endtry)
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
        //process json
        try
        {
            JSONObject jsonRoot = new JSONObject(result.toString());
            ageOfLastEntry = jsonRoot.getInt("last_data_age");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(ageOfLastEntry >= timeRequireFeback)
               return Chanel.GATEWAY_OOFLINE;

        else
                return Chanel.GATEWAY_ONLINE;
    }

    private Device creatDevice(String name,String chanelID,int i){
        Device device = new Device();
        device.setName(name);
        device.setId(String.valueOf(i));
        InputStream inputStream = null;
        BufferedReader buff = null;
            String address = "https://api.thingspeak.com/channels/" + chanelID + "/fields/" + i + "/last.json";
        StringBuilder result =  new StringBuilder();
        //get json from sever (last entry infomation)
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

        if(!(result.toString().equals("\"1\"") || result.toString().equals("1")))
        {
            try {
                JSONObject jsonRoot = new JSONObject(result.toString());
                Double value = jsonRoot.getDouble("field" + i);
                String strTime = jsonRoot.getString("created_at");
                GregorianCalendar time = convertTime.getTime(strTime);
                GregorianCalendar rightNow =(GregorianCalendar) GregorianCalendar.getInstance();
                if((rightNow.getTimeInMillis() - time.getTimeInMillis()) >= 300000)
                    device.setStatus(Device.DEVICE_OOFLINE);
                else
                    device.setStatus(Device.DEVICE_ONLINE);
                device.setLastEntryTime(time);
                device.setLastEntryValue(value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
            device.setStatus(Device.DEVICE_UNDEFINED);
        return device;

    }

}
