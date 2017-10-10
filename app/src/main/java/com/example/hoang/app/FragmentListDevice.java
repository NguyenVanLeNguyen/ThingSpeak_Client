package com.example.hoang.app;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.hoang.app.R.layout.item;

/**
 * Created by nguyenhoang on 10/10/2017.
 */

public class FragmentListDevice extends Fragment {

    public ListView lvDevice ;
    public MainActivity parent;
    public CustomAdapter custem;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof  MainActivity){
            parent = (MainActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.list_device_fragment, container, false);
       lvDevice = (ListView) view.findViewById(R.id.liv1);

        return view;
    }

    public void setOnListView(ArrayList<Device> listDevice){
        custem = new CustomAdapter(parent, item,listDevice);
        lvDevice.setAdapter(custem);
        final ArrayList<Device> finalListDevice = listDevice;
        lvDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                provideGraph(finalListDevice.get(i));
            }

        });
    }

    public void provideGraph(Device devi){
        Intent intent = new Intent(parent,GraphActivity.class);
        intent.putExtra(MainActivity.DEVICE,devi);
        startActivity(intent);
    }

    public CustomAdapter getCustem() {
        return custem;
    }


}
