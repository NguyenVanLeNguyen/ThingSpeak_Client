package com.example.hoang.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

import java.util.ArrayList;
import java.util.GregorianCalendar;


/**
 * Created by hoang on 10/09/2017.
 */

public class Device implements Parcelable {
    private String APIkey;
    //private int color;
    private ArrayList<Pair<GregorianCalendar,Integer>> data ;

    public Device(String apikey,ArrayList<Pair<GregorianCalendar,Integer>> data){
        this.APIkey = apikey;
        this.data = data;

    }

    protected Device(Parcel in) {
        APIkey = in.readString();
    }


    public String getAPIkey() { return APIkey; }

    public ArrayList<Pair<GregorianCalendar,Integer>> getData() {
        return data;
    }

    public void setAPIkey(String apikey) {
        this.APIkey = apikey;
    }

    public void setData(ArrayList<Pair<GregorianCalendar,Integer>> data) {
        this.data = data;
    }


    public static final Creator<Device> CREATOR = new Creator<Device>() {
        @Override
        public Device createFromParcel(Parcel in) {
            return new Device(in);
        }

        @Override
        public Device[] newArray(int size) {
            return new Device[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(APIkey);
        parcel.

    }


}
