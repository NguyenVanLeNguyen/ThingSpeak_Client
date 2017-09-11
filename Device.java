package com.example.hoang.datafromserver;

/**
 * Created by hoang on 10/09/2017.
 */

public class Device {
    private String APIkey;

    public Device(String apikey){
        APIkey = apikey;
    }

    public void setAPIkey(String apikey) {
        this.APIkey = apikey;
    }

    public String getAPIkey() {

        return APIkey;
    }
}
