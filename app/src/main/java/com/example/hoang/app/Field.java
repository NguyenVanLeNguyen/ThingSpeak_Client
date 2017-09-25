package com.example.hoang.app;

/**
 * Created by hoang on 11/09/2017.
 */

public class Field {
    private String APIkey;

    private int Number;

    public Field(String apikey,int number){
        APIkey = apikey;
        Number = number;
    }

    public String getAPIkey(){
        return  APIkey;
    }

    public  int getNumber(){
        return Number;
    }

    public void setAPIkey(String apikey){
        APIkey = apikey;
    }

    public void setNumber(int number){
        Number = number;
    }
}
