package com.example.hoang.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * Created by hoang on 10/09/2017.
 */

public class Device implements Parcelable {
    private String Name;
    private float lastEntryValue;
    private GregorianCalendar lastEntryTime;
    private ArrayList<Pair<GregorianCalendar,Integer>> data ;

    public Device(String name,ArrayList<Pair<GregorianCalendar,Integer>> data){
        this.Name = name;
        this.data = data;
    }

    public Device(String name, float lastEntryValue, GregorianCalendar lastEntryTime) {
        Name = name;
        this.lastEntryValue = lastEntryValue;
        this.lastEntryTime = lastEntryTime;
    }



    protected Device(Parcel in) {
        Name = in.readString();
        int size = in.readInt();
        data = new ArrayList<Pair<GregorianCalendar,Integer>>();
        for(int j = 0 ; j < size; j++){
            GregorianCalendar date = (GregorianCalendar) in.readValue(GregorianCalendar.class.getClassLoader());
            Integer value = (Integer) in.readValue(Integer.class.getClassLoader());
            Pair<GregorianCalendar,Integer> unit = Pair.create(date,value);
            data.add(unit);
        }
       // data = new ArrayList<Pair<GregorianCalendar,Integer>>();
        //data = in.readArrayList(Pair.class.getClassLoader());
    }


    public String getAPIkey() { return Name; }

    public List<Pair<GregorianCalendar,Integer>> getData() {
        return data;
    }

    public void setAPIkey(String name) {
        this.Name = name;
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(Name);
        parcel.writeInt(data.size());
        for(int j = 0 ; j < data.size(); j ++){
            parcel.writeValue(data.get(j).first);
            parcel.writeValue(data.get(j).second);
        }
        //parcel.writeList(data);
        //parcel.write;
    }


}
