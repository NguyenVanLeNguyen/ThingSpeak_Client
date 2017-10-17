package com.example.hoang.app;

import java.util.ArrayList;

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

    public Chanel getInstance(){
        if(chanel != null){
            chanel = new Chanel();
        }
        return chanel;
    }


}
