package com.example.hoang.app;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

import static android.R.attr.value;

public class GraphActivity extends AppCompatActivity {

    private static final String TAG = "devi:";
    private  String[] days = new String[7];
    private int[] valuesEachWeek = new int[7];
    private String[] hours = new String[24];
    private float[] valuesEachHour = new float[24];
    ProcessingTime processer;
    //private Spinner spinner;
    private Device devi;

    private LineChartView chartTop;
    private ColumnChartView chartBottom;
    ArrayList<GregorianCalendar> thisWeek;
    private LineChartData lineData;
    private ColumnChartData columnData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        //Get the Data What MainActivity sent
        Bundle intent = getIntent().getExtras();
        devi = (Device) intent.getParcelable(MainActivity.DEVICE);


       // for()
        //Log.d(TAG, String.valueOf(devi.getData().size()));
        chartTop = (LineChartView) findViewById(R.id.chart_top);

        // Generate and set data for line chart

        // *** BOTTOM COLUMN CHART ***
        chartBottom = (ColumnChartView) findViewById(R.id.chart_bottom);

        getSevenDay();
        generateColumaData();
        generateInitialLineData();

    }

    public void getSevenDay(){

        DateFormat formatDayOfMonth = new SimpleDateFormat("dd/MM");
        processer = new ProcessingTime();

        int lastIndex = devi.getData().size() - 1;
        if(lastIndex < 0){
            return;
        }
        thisWeek = processer.getSevenDay(devi.getData().get(lastIndex).first);
        for(int i = 0 ; i <= 6; i++){
            days[i] = formatDayOfMonth.format(thisWeek.get(i).getTime());
            Log.d(TAG,days[i]);
        }
        int j = 6;
        int index = lastIndex;
        while(j >= 0 ){
            int init_J = j;
            GregorianCalendar day1 = thisWeek.get(j);

            for(int k = index; k >= 0; k--){
                GregorianCalendar day2 = devi.getData().get(k).first;
                if((day1.get(Calendar.YEAR) == day2.get(Calendar.YEAR))
                && (day1.get(Calendar.MONTH) == day2.get(Calendar.MONTH))
                && (day1.get(Calendar.DATE) == day2.get(Calendar.DATE))){
                    valuesEachWeek[j] = devi.getData().get(k).second;
                    Log.d(TAG,String.valueOf(valuesEachWeek[j]));
                    j--;
                    index = k;
                    break;
                }

            }
            if(j == init_J)
                j--;
        }
    }



    public void generateColumaData(){
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> vals;
        for(int i = 0 ; i <= 6; i++ ){
            vals = new ArrayList<>();
            vals.add(new SubcolumnValue((float)valuesEachWeek[i], ChartUtils.pickColor()));

            axisValues.add(new AxisValue(i).setLabel(days[i]));
            columns.add(new Column( vals).setHasLabelsOnlyForSelected(true));
        }
        columnData = new ColumnChartData(columns);
        columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(2));
        chartBottom.setColumnChartData(columnData);
        chartBottom.setOnValueTouchListener(new ValueTouchListener());
        chartBottom.setValueSelectionEnabled(true);
        chartBottom.setZoomType(ZoomType.HORIZONTAL);
    }

    private void generateInitialLineData() {
        int numValues = 24;

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<PointValue> values = new ArrayList<PointValue>();
        for (int i = 0; i < numValues; ++i) {
            values.add(new PointValue(i, 0));
            axisValues.add(new AxisValue(i).setLabel(String.valueOf(i)));
        }

        Line line = new Line(values);
        line.setColor(ChartUtils.COLOR_GREEN).setCubic(true);

        List<Line> lines = new ArrayList<>();
        lines.add(line);

        lineData = new LineChartData(lines);
        lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true).setMaxLabelChars(1));
        lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));

        chartTop.setLineChartData(lineData);

        // For build-up animation you have to disable viewport recalculation.
        chartTop.setViewportCalculationEnabled(false);

        // And set initial max viewport and current viewport- remember to set viewports after data.
        Viewport v = new Viewport(0, 110, 23, 0);
        chartTop.setMaximumViewport(v);
        chartTop.setCurrentViewport(v);

        chartTop.setScrollEnabled(true);
        chartTop.setZoomType(ZoomType.HORIZONTAL);
    }

    private void generateLineData(int color, float range,int columIndex) {
        // Cancel last animation if not finished.
        int numValues = 24;

        chartTop.cancelDataAnimation();

        // Modify data targets
        Line line = lineData.getLines().get(0);// For this example there is always only one line.
        line.setColor(color);
        //PointValue values = line.getValues();
        List<PointValue> values = new ArrayList<PointValue>();
        line.setValues(values);

        setDataLine(columIndex);

        for(int i = 0; i < numValues; i++){

            if(valuesEachHour[i] == 0){

            }
            else if(valuesEachHour[i] == -1){
                values.add(new PointValue().setTarget((float)i,(float)0));
            }
            else{
                values.add(new PointValue().setTarget((float)i,valuesEachHour[i]));
            }

            valuesEachHour[i] = 0;
        }

        /*for(PointValue value : line.getValues()){
            if(value.getY() == -1.0       )
                line.getValues().remove(value);
            //
        }
        for(int i = 0; i < numValues; i++){
            valuesEachHour[i] = 0;
        }
        /*for (PointValue value : line.getValues()) {
            // Change target only for Y value.
                value.setTarget(value.getX(), (float) Math.random() * range);
        }*/

        // Start new data animation with 300ms duration;
        chartTop.startDataAnimation(300);
    }

    private void setDataLine(int columIndex){
        int lastIndex = devi.getData().size() - 1;
        GregorianCalendar date = thisWeek.get(columIndex);
        //date.set(Calendar.);
        int index = lastIndex;
        for(int i = index;i >= 0;i--){
            GregorianCalendar day2 = devi.getData().get(i).first;
            if((date.get(Calendar.YEAR) == day2.get(Calendar.YEAR))
             &&(date.get(Calendar.MONTH) == day2.get(Calendar.MONTH))
             &&(date.get(Calendar.DATE) == day2.get(Calendar.DATE))){
                /*PointValue value =line.getValues().get(day2.get(Calendar.HOUR));
                value.setTarget(value.getX(),(float) devi.getData().get(i).second);*/
                if(devi.getData().get(i).second == 0){
                    valuesEachHour[day2.get(Calendar.HOUR)] = -1;
                }
                else
                    valuesEachHour[day2.get(Calendar.HOUR)] = (float) devi.getData().get(i).second;
                index = i;
                //break;
            }
        }

    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            generateLineData(value.getColor(), 100,columnIndex);
        }

        @Override
        public void onValueDeselected() {
            generateLineData(ChartUtils.COLOR_GREEN, 0,0);

        }
    }
}

