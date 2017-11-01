package com.example.hoang.app;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;

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

import java.util.GregorianCalendar;


/**
 * Created by hoang on 16/10/2017.
 */

public class GetChanel extends AsyncTask<String,String,Chanel>
{
    private ProgressDialog progressDialog;
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
        content = getJsonChanelfromUrl(bun[0]);
        Chanel chanel = null ;
        if(!content.equals("erron"))
        {
            chanel = new Chanel();
            chanel.setAPIkey(bun[0]);
            checkStatusGateway(chanel);


            try
            {
                JSONObject jsonRoot = new JSONObject(content);
                JSONArray fields = jsonRoot.getJSONArray("tags");
                String name = jsonRoot.getString("name");
                Double longitude = jsonRoot.getDouble("longitude");
                Double latitude = jsonRoot.getDouble("latitude");

                chanel.setName(name);
                chanel.setLatitude(latitude);
                chanel.setLongitude(longitude);

                for(int i = 0; i < fields.length(); i++ ){
                    int j = i+1;
                    String nameDevi = fields.getJSONObject(i).getString("name");
                    //String nameDevi = fields.getJSONObject(i).getString(field);

                    Device device = creatDevices(nameDevi,bun[0],j);
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
        if(result == null) {
            Toast.makeText(mainactivity,"ID is unvailable",Toast.LENGTH_LONG).show();

            mainactivity.displayDialogChanelID();
        }
        else {
            mainactivity.getListDevice().addAll(result.getFields());
            mainactivity.setList();
        }

        TextView gateWay_name = (TextView) mainactivity.findViewById(R.id.txv_name_gateway);
        TextView gateWay_status = (TextView) mainactivity.findViewById(R.id.txv_status_gateway);
        TextView gateWay_update = (TextView) mainactivity.findViewById(R.id.txv_lastupdate_gateway);

        assert result != null;
        gateWay_name.setText(result.getAPIkey());
        if(result.getStatus() == Chanel.GATEWAY_ONLINE){
            gateWay_status.setText(R.string.status_on);
            gateWay_status.setTextColor(Color.BLUE);
        }
        else{
            gateWay_status.setText(R.string.status_off);
            gateWay_status.setTextColor(Color.RED);
        }
        gateWay_update.setText("Update at: " + formatTimeJson.format(result.getLastUpdate().getTime()));
        CallbackMap callbackMap = new CallbackMap(result.getLatitude(),result.getLongitude(),"Location of"+result.getAPIkey(),"Unknow");
        SupportMapFragment mapFragment = (SupportMapFragment) mainactivity.getSupportFragmentManager()
                .findFragmentById(R.id.mapid);
        mapFragment.getMapAsync(callbackMap);

        String[] DeviceNames  = new String[result.getFields().size()];
        int i = 0;
        for(Device devi : result.getFields()){
            DeviceNames[i] = devi.getName();
            i++;
        }
        mainactivity.setDEVICES(DeviceNames);

        //mainactivity.startJob();
        //mainactivity.searchViewCode();


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

    //Check gateway's status
    private void checkStatusGateway(Chanel chanel)
    {
        int timeRequireFeback = 300;
        int ageOfLastEntry = 301 ;
        InputStream inputStream = null;
        BufferedReader buff = null;
        String address = "https://api.thingspeak.com/channels/" + chanel.getAPIkey() +"/feeds/last_data_age.json";
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
            long timeUpdateMilis = ageOfLastEntry * 1000;
            GregorianCalendar timeUpdate = new GregorianCalendar();
            timeUpdate.setTimeInMillis(timeUpdate.getTimeInMillis() - timeUpdateMilis);
            chanel.setLastUpdate(timeUpdate);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(ageOfLastEntry >= timeRequireFeback)
             chanel.setStatus(Chanel.GATEWAY_OOFLINE);

        else
             chanel.setStatus(Chanel.GATEWAY_ONLINE);
    }

    private Device creatDevices(String name,String chanelID,int i){
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

        if(!(result.toString().equals("\"1\"") || result.toString().equals("-1")))
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
                device.setAPIchanel(chanelID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
            device.setStatus(Device.DEVICE_UNDEFINED);
        return device;

    }

}
