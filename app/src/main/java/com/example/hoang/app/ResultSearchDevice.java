package com.example.hoang.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.hoang.app.R.layout.item;

public class ResultSearchDevice extends AppCompatActivity {
    private ListView listResult ;
    private CustomAdapter custem;
    private ArrayList<Device> arrResult;
    MainActivity mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);
        listResult = (ListView) findViewById(R.id.liv_listResult);

        arrResult = new ArrayList<>();
        Bundle intent = getIntent().getExtras();
        arrResult = intent.getParcelableArrayList(MainActivity.RESULTSEARCH);

        custem = new CustomAdapter(this, item,arrResult);
        listResult.setAdapter(custem);
        final ArrayList<Device> finalListDevice = arrResult;

        listResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(finalListDevice.get(i) == null){
                }
                else{
                    GetFieldFeed loadingField = new GetFieldFeed(ResultSearchDevice.this);
                    loadingField.execute(finalListDevice.get(i));
                }

            }

        });

    }
}
