package com.example.hoang.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoang.AboutNetWork.ConnectionReceiver;
import com.example.hoang.Component.Device;

import java.util.ArrayList;
import static com.example.hoang.app.R.layout.item;


public class ResultSearchActivity extends ShowListDevice {
    private ListView listResult ;
    private CustomAdapter custem;
    private TextView numOfResult;
    private ArrayList<Device> arrResult;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);
        listResult = (ListView) findViewById(R.id.liv_listResult);
        numOfResult = (TextView) findViewById(R.id.txv_number_of_result);
        arrResult = new ArrayList<>();
        Bundle intent = getIntent().getExtras();
        assert intent != null;
        arrResult = intent.getParcelableArrayList(MainActivity.RESULTSEARCH);

        custem = new CustomAdapter(this, item,arrResult);
        listResult.setAdapter(custem);
        numOfResult.setText(arrResult.size()+"");
        final ArrayList<Device> finalListDevice = arrResult;

        listResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(ConnectionReceiver.isConnected()) {
                    if (finalListDevice.get(i) != null) {
                        GetFieldFeed loadingField = new GetFieldFeed(ResultSearchActivity.this);
                        loadingField.execute(finalListDevice.get(i));
                    }
                }
                else
                    Toast.makeText(ResultSearchActivity.this,"network error",Toast.LENGTH_SHORT).show();


            }

        });
    }

    @Override
    public void provideGraph(Device devi){
        Intent intent = new Intent(ResultSearchActivity.this,GraphActivity.class);
        intent.putExtra(DEVICE, devi);
        startActivity(intent);
    }



}
