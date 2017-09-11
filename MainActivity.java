package com.example.hoang.datafromserver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lvDevice ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvDevice = (ListView) findViewById(R.id.liv1);
        ArrayList<Device> listDevice = new ArrayList<>();
        listDevice.add(new Device("A3v2vdee515"));
        listDevice.add(new Device("S2geb6r5355"));
        listDevice.add(new Device("F4vneru5775"));

        CustomAdapter custem = new CustomAdapter(this,R.layout.item,listDevice);
        lvDevice.setAdapter(custem);
    }
}
