package com.example.hoang.app;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

public class GraphActivity extends AppCompatActivity {

    private static final String TAG = "devi:";
    public  String[] days = new String[7];
    public int[] values = new int[7];
    ProcessingTime processer;
    //private Spinner spinner;
    private Device devi;

    private LineChartView chartTop;
    private ColumnChartView chartBottom;

    private LineChartData lineData;
    private ColumnChartData columnData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);


        //Get the Data What MainActivity sent
        Bundle intent = getIntent().getExtras();
        devi = (Device) intent.getParcelable(MainActivity.DEVICE);

        if(devi.getData() == null){
            Log.d(TAG,"devi is null");
        }
        else
            Log.d(TAG,"devi is not null");
        Log.d(TAG,"devi APIKEY"+devi.getAPIkey());

        //Log.d(TAG, String.valueOf(devi.getData().size()));
        chartTop = (LineChartView) findViewById(R.id.chart_top);

        // Generate and set data for line chart

        // *** BOTTOM COLUMN CHART ***

        chartBottom = (ColumnChartView) findViewById(R.id.chart_bottom);

        //getSevenDay();
        //generateColumaData();
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

        int lastIndex = devi.getData().size() - 1;
        if(lastIndex < 0){
            return;
        }
        ArrayList<GregorianCalendar> thisWeek = processer.getSevenDay(devi.getData().get(lastIndex).first);
        for(int i = 6 ; i >= 0; i++){
            days[i] = formatDayOfMonth.format(thisWeek.get(i));
        }
        int j = 6;
        while(j < 0){
            GregorianCalendar day1 = thisWeek.get(j);
            for(int index = lastIndex; index >= 0; index--){
                GregorianCalendar day2 = devi.getData().get(index).first;

                if((day1.get(Calendar.YEAR) == day2.get(Calendar.YEAR))
                && (day1.get(Calendar.MONTH) == day2.get(Calendar.MONTH))
                && (day1.get(Calendar.DATE) == day2.get(Calendar.DATE))){
                    values[j] = devi.getData().get(index).second;
                    j--;
                    break;
                }
            }
        }
    }



    public void generateColumaData(){
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        //List<SubcolumnValue> values;
        for(int i = 6 ; i >= 0; i-- ){
            //values = new ArrayList<SubcolumnValue>();
            axisValues.add(new AxisValue(i).setLabel(days[i]));
            columns.add(values[i],new Column());
        }
        columnData = new ColumnChartData(columns);
        columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(2));
        chartBottom.setColumnChartData(columnData);
        //chartBottom.setOnValueTouchListener(new ValueTouchListener());
        chartBottom.setValueSelectionEnabled(true);

        chartBottom.setZoomType(ZoomType.HORIZONTAL);
    }

    }

