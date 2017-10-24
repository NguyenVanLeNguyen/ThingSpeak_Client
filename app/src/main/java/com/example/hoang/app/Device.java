package com.example.hoang.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * Created by hoang on 10/09/2017.
 */

public class Device implements Parcelable {
    public final static int DEVICE_ONLINE = 1;
    public final static int DEVICE_OOFLINE = 0;
    public final static int DEVICE_UNDEFINED = -1;


    private String Name;
    private String APIchanel;
    private String id;
    private int status;
    private Double lastEntryValue;
    private GregorianCalendar lastEntryTime;
    private ArrayList<Pair<GregorianCalendar,Double>> data ;

    public Device(){

    }

    public Device(String name,ArrayList<Pair<GregorianCalendar,Double>> data){
        this.Name = name;
        this.data = data;
    }

    public Device(String name, Double lastEntryValue, GregorianCalendar lastEntryTime) {
        Name = name;
        this.lastEntryValue = lastEntryValue;
        this.lastEntryTime = lastEntryTime;
    }



    protected Device(Parcel in) {
        Name = in.readString();
        int size = in.readInt();
        data = new ArrayList<Pair<GregorianCalendar,Double>>();
        for(int j = 0 ; j < size; j++){
            GregorianCalendar date = (GregorianCalendar) in.readValue(GregorianCalendar.class.getClassLoader());
            Double value = (Double) in.readValue(Double.class.getClassLoader());
            Pair<GregorianCalendar,Double> unit = Pair.create(date,value);
            data.add(unit);
        }
        // data = new ArrayList<Pair<GregorianCalendar,Integer>>();
        //data = in.readArrayList(Pair.class.getClassLoader());
    }

    public List<Pair<GregorianCalendar,Double>> getData() {
        return data;
    }

    public void setData(ArrayList<Pair<GregorianCalendar,Double>> data) {
        this.data = data;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Double getLastEntryValue() {
        return lastEntryValue;
    }

    public void setLastEntryValue(Double lastEntryValue) {
        this.lastEntryValue = lastEntryValue;
    }

    public GregorianCalendar getLastEntryTime() {
        return lastEntryTime;
    }

    public void setLastEntryTime(GregorianCalendar lastEntryTime) {
        this.lastEntryTime = lastEntryTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAPIchanel() {
        return APIchanel;
    }

    public void setAPIchanel(String APIchanel) {
        this.APIchanel = APIchanel;
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

    }
    @Override
    public String toString() {
        return this.Name + this.status;
    }

}
