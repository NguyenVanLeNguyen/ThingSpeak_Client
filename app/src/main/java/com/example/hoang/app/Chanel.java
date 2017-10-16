package com.example.hoang.app;

import java.util.ArrayList;

/**
 * Created by hoang on 14/10/2017.
 */

public class Chanel
{
    private String APIkey;

    private String name;

    private double longitude;

    private double latitude;

    private ArrayList<Device> fields;

    private Chanel chanel;

    public Chanel() {
    }

    public Chanel(String APIkey, String name,double longitude,double latitude) {
        this.APIkey = APIkey;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public Chanel getInstance(){
        if(chanel != null){
            chanel = new Chanel();
        }
        return chanel;
    }
}
