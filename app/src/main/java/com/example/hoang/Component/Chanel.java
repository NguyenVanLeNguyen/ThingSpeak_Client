package com.example.hoang.Component;

import com.example.hoang.app.ProcessingTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by hoang on 14/10/2017.
 */

public class Chanel
{
    public final static int GATEWAY_OOFLINE = 0;
    public final static int GATEWAY_ONLINE = 1;
    public final static int GATEWAY_UNDEFINED = -1;
    private String APIkey;

    private String name;

    private int status;

    private double longitude;

    private double latitude;

    private GregorianCalendar lastUpdate;

    private ArrayList<Device> fields;

    private Chanel chanel;

    public Chanel() {
        this.fields = new ArrayList<>();
    }

    public Chanel(String APIkey, String name,double longitude,double latitude) {
        this.APIkey = APIkey;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Chanel(String APIkey, String name, double longitude, double latitude,String lastUpdate) {
        this.APIkey = APIkey;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        DateFormat formatTimeJson = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        ProcessingTime converter = new ProcessingTime();
        converter.setFormat(formatTimeJson);
        this.lastUpdate = converter.getTime(lastUpdate);
    }

    public String getAPIkey() {
        return APIkey;
    }

    public void setAPIkey(String APIkey) {
        this.APIkey = APIkey;
    }

    public ArrayList<Device> getFields() {
        return fields;
    }

    public void setFields(ArrayList<Device> fields) {
        this.fields = fields;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public GregorianCalendar getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(GregorianCalendar lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Chanel getInstance(){
        if(chanel != null){
            chanel = new Chanel();
        }
        return chanel;
    }


}
