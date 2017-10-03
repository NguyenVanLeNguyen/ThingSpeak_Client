package com.example.hoang.app;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.data.Entry;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.SubcolumnValue;

public class GraphActivity extends AppCompatActivity {
    public  String[] days ;
    private ArrayList<GregorianCalendar> thisWeek;
    ProcessingTime processer;
    private Spinner spinner;
    private Device devi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);


        //Get the Data What MainActivity sent
        Bundle intent = getIntent().getExtras();
        devi = (Device) intent.getParcelable(MainActivity.DEVICE);
       /* spinner = (Spinner) findViewById(R.id.sp_mode);

        //Get the Data What MainActivity sent
        Bundle intent = getIntent().getExtras();
        devi = (Device) intent.getParcelable(MainActivity.DEVICE);

        //Processing Spiner
        ArrayList<String> Modes = new ArrayList<String>();
        Modes.add("To Day");
        Modes.add("This Week");
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,Modes);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/



    }

    public void getSevenDay(){
        DateFormat formatDayOfMonth = new SimpleDateFormat("dd/MM");
        processer = new ProcessingTime();
        processer.setFormatDayOfMonth(formatDayOfMonth);
        int lastIndex = devi.getData().size() - 1;
        days = processer.getSevenDay(devi.getData().get(lastIndex).first);

    }



    public void generateColumaData(){


        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for(int i = 6 ; i >= 0; i-- ){
            values = new ArrayList<SubcolumnValue>();

        }
    }

}
