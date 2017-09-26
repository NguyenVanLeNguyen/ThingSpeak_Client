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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class GraphActivity extends AppCompatActivity {

    private LineChart mChart;
    private Spinner spinner;
    private Device devi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        mChart = (LineChart) findViewById(R.id.lch_level);
        spinner = (Spinner) findViewById(R.id.sp_mode);

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
        });

        setData();
        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        mChart.setTouchEnabled(true);

        XAxis xa = mChart.getXAxis();
        xa.setAxisMaximum(24f);

    }


    public ArrayList<Entry> alterTime(ArrayList<Pair<GregorianCalendar,Integer>> list){
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        int SizeofData = list.size();
        GregorianCalendar now = list.get(SizeofData - 1).first;
        GregorianCalendar thisDay = new GregorianCalendar();
        return  yVals;
    }
    private ArrayList<String> setXAxisValues(){
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("10");
        xVals.add("20");
        xVals.add("30");
        xVals.add("30.5");
        xVals.add("40");

        return xVals;
    }

    private ArrayList<Entry> setYAxisValues_ToDay(){
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        /*int numOfEntry = devi.getData().size() - 1;

        for(int i =  numOfEntry;i >= 0 ; i--  ){

        }*/
        yVals.add(new Entry(0, 60));
        yVals.add(new Entry(1, 48));
        yVals.add(new Entry(2,70.5f ));
        yVals.add(new Entry(3, 100));
        yVals.add(new Entry(4, 80.9f));

        return yVals;
    }

    private ArrayList<Entry> setYAxisValues_ThisWeek(){
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        /*int numOfEntry = devi.getData().size() - 1;

        for(int i =  numOfEntry;i >= 0 ; i--  ){

        }*/
        yVals.add(new Entry(0, 60));
        yVals.add(new Entry(1, 48));
        yVals.add(new Entry(2,70.5f ));
        yVals.add(new Entry(3, 100));
        yVals.add(new Entry(4, 80.9f));

        return yVals;
    }

    private void setData() {
        ArrayList<String> xVals = setXAxisValues();

        ArrayList<Entry> yVals = setYAxisValues_ToDay();

        LineDataSet set1;

        // create a dataset and give it a type
        set1 = new LineDataSet(yVals, "DataSet 1");

        set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        //   set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(dataSets);

        // set data
        mChart.setData(data);

    }

}
