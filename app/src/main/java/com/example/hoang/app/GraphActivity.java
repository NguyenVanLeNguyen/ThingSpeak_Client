package com.example.hoang.app;


import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.TextView;

import com.example.hoang.Component.Device;

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


public class GraphActivity extends AppCompatActivity {

    private  String[] days = new String[7];
    private Double[] valuesEachWeek = new Double[7];
    private float[] valuesEachHour = new float[24];
    ProcessingTime processer;
    private Device devi;
    private LineChartView chartTop;
    private ColumnChartView chartBottom;
    ArrayList<GregorianCalendar> thisWeek;
    private LineChartData lineData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        for(int i = 0 ; i<7 ; i++){
            valuesEachWeek[i] = -1.0;
            days[i] = "";
        }
        //Get the Data What MainActivity sent
        Bundle intent = getIntent().getExtras();

        assert intent != null;
        devi = intent.getParcelable(MainActivity.DEVICE);

        TextView tvNameDevi = (TextView) findViewById(R.id.device_name);
        tvNameDevi.setText(devi.getName());

        chartTop = (LineChartView) findViewById(R.id.chart_top);

        // Generate and set data for line chart

        // *** BOTTOM COLUMN CHART ***
        chartBottom = (ColumnChartView) findViewById(R.id.chart_bottom);
        getSevenDay();
        generateColumaData();
        generateInitialLineData();


    }

    public void getSevenDay(){

        @SuppressLint("SimpleDateFormat")
        DateFormat formatDayOfMonth = new SimpleDateFormat("dd/MM");
        processer = new ProcessingTime();

        int lastIndex = devi.getData().size() - 1;
        if(lastIndex < 0){
            return;
        }
        thisWeek = processer.getSevenDay(devi.getData().get(lastIndex).first);
        for(int i = 0 ; i <= 6; i++){
            days[i] = formatDayOfMonth.format(thisWeek.get(i).getTime());

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
        List<AxisValue> axisValues = new ArrayList<>();
        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> vals;
        for(int i = 0 ; i <= 6; i++ ){
            vals = new ArrayList<>();
            if(valuesEachWeek[i] > 0)
                vals.add(new SubcolumnValue(valuesEachWeek[i].floatValue(), ChartUtils.pickColor()));

            axisValues.add(new AxisValue(i).setLabel(days[i]));
            columns.add(new Column( vals).setHasLabelsOnlyForSelected(true));
        }
        ColumnChartData columnData = new ColumnChartData(columns);
        columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(2));
        chartBottom.setColumnChartData(columnData);
        chartBottom.setOnValueTouchListener(new ValueTouchListener());
        chartBottom.setValueSelectionEnabled(true);
        chartBottom.setZoomType(ZoomType.HORIZONTAL);
    }

    private void generateInitialLineData() {
        int numValues = 24;

        List<AxisValue> axisValues = new ArrayList<>();
        List<PointValue> values = new ArrayList<>();
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
        chartTop.setValueSelectionEnabled(true);
        line.setHasLabelsOnlyForSelected(true);
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

    private void generateLineData(int color,int columIndex) {
        // Cancel last animation if not finished.
        int numValues = 24;

        chartTop.cancelDataAnimation();

        // Modify data targets
        Line line = lineData.getLines().get(0);// For this example there is always only one line.
        line.setColor(color);
        List<PointValue> values = new ArrayList<>();
        line.setValues(values);

        if(columIndex >= 0){
            setDataLine(columIndex);

            for(int i = 0; i < numValues; i++){

                if(valuesEachHour[i] == -1){
                    values.add(new PointValue().setTarget((float)i,(float)0));
                }
                else if(valuesEachHour[i] != 0){
                    values.add(new PointValue().setTarget((float)i,valuesEachHour[i]));
                }

                valuesEachHour[i] = 0;
            }

        }
        chartTop.startDataAnimation(300);
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

    }

    private void setDataLine(int columIndex){
        int lastIndex = devi.getData().size() - 1;
        GregorianCalendar date = (GregorianCalendar) thisWeek.get(columIndex).clone();
        GregorianCalendar dateToCompare =(GregorianCalendar) thisWeek.get(columIndex).clone() ;
        dateToCompare.set(Calendar.HOUR_OF_DAY,0);
        dateToCompare.set(Calendar.MINUTE,0);
        dateToCompare.set(Calendar.SECOND,0);
        for(int i = lastIndex;i >= 0;i--){
            GregorianCalendar dateItem =(GregorianCalendar) devi.getData().get(i).first.clone() ;

            if(dateItem.compareTo(dateToCompare) < 0)
                break;
            if((date.get(Calendar.YEAR) == dateItem.get(Calendar.YEAR))
             &&(date.get(Calendar.MONTH) == dateItem.get(Calendar.MONTH))
             &&(date.get(Calendar.DATE) == dateItem.get(Calendar.DATE))){
                if(devi.getData().get(i).second == 0){
                    valuesEachHour[dateItem.get(Calendar.HOUR_OF_DAY)] = -1;
                }
                else
                    valuesEachHour[dateItem.get(Calendar.HOUR_OF_DAY)] = devi.getData().get(i).second.floatValue();
            }
        }

    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            generateLineData(value.getColor(),columnIndex);
        }

        @Override
        public void onValueDeselected() {
            generateLineData(ChartUtils.COLOR_GREEN,-1);

        }
    }
}

