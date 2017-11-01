package com.example.hoang.app;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by hoang on 10/09/2017.
 */

public class CustomAdapter extends ArrayAdapter<com.example.hoang.app.Device> {
    private int Resource;
    private ArrayList<Device> Objects;
    private ArrayList<Device> Backup;
    private Context con;
    private ProcessingTime convertTime;
    private DateFormat formatTimeJson = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public CustomAdapter(Context context, int resource, ArrayList<Device> object){
        super(context,resource,object);
        con = context;
        Resource = resource;
        Objects = object;
        convertTime = new ProcessingTime();
        convertTime.setFormat(formatTimeJson);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup data){

        ViewHolder viewholder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item,null,true);
            viewholder = new ViewHolder();
            viewholder.tv1 =  convertView.findViewById(R.id.txv_name);
            viewholder.tv2 =  convertView.findViewById(R.id.txv_value);
            viewholder.tv3 =  convertView.findViewById(R.id.txv_status);
            viewholder.tv4 =  convertView.findViewById(R.id.txv_lastTime);
            viewholder.lilao =  convertView.findViewById(R.id.lnlao_1);
            viewholder.process =  convertView.findViewById(R.id.processbar);
            convertView.setTag(viewholder);

        }else {
            viewholder  = (ViewHolder) convertView.getTag();
        }
        Device device;
        device = Objects.get(position);
        Double level = device.getLastEntryValue();
        Date date = device.getLastEntryTime().getTime();
        String timeUpdate = formatTimeJson.format(date) ;
        viewholder.tv1.setText( device.getName());

        viewholder.tv2.setText("value: "+String.valueOf(level));

        if(device.getStatus() == 1){
            viewholder.tv3.setText(R.string.status_on);
            viewholder.tv3.setTextColor(Color.BLUE);
        }
        else if(device.getStatus() == 0){
            viewholder.tv3.setText(R.string.status_off);
            viewholder.tv3.setTextColor(Color.RED);
        }
        else{
            viewholder.tv3.setText(R.string.status_undefine);
            viewholder.tv3.setTextColor(Color.GRAY);
        }
        viewholder.process.setProgress(level.floatValue());

        viewholder.tv4.setText("update at: "+ timeUpdate );
        if(level < 20 && level >= 0) {
            viewholder.process.setProgressColor(this.getContext().getResources().getColor(R.color.Dangerous));
        }
        else if(level >= 20 && level < 60) {
            viewholder.process.setProgressColor(this.getContext().getResources().getColor(R.color.Medidum));
        }
        else{
            viewholder.process.setProgressColor(this.getContext().getResources().getColor(R.color.Safe));
        }

        return convertView;
    }

    private class ViewHolder{
        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;
        LinearLayout lilao;
        RoundCornerProgressBar process;
    }

    public ArrayList<Device> getObjects() {
        return Objects;
    }
}
