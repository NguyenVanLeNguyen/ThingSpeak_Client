package com.example.hoang.app;

import android.support.v7.app.AppCompatActivity;

import java.util.List;

/**
 * Created by nguyenhoang on 01/11/2017.
 */

public abstract class ShowListDevice extends AppCompatActivity {
    public static final String DEVICE = "DeviceSelect";
    public abstract void provideGraph(Device device);

}
