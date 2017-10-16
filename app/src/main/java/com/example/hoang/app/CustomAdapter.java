package com.example.hoang.app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.github.lzyzsd.circleprogress.CircleProgress;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by hoang on 10/09/2017.
 */

public class CustomAdapter extends ArrayAdapter<com.example.hoang.app.Device> {
    private int Resource;
    private ArrayList<Device> Objects;
    private Context con;

    public CustomAdapter(Context context, int resource, ArrayList<Device> object){
        super(context,resource,object);
        con = context;
        Resource = resource;
        Objects = object;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup data){

        ViewHolder viewholder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item,null,true);
            viewholder = new ViewHolder();
            viewholder.tv1 =  convertView.findViewById(R.id.tv_1);
            viewholder.tv2 =  convertView.findViewById(R.id.tv_2);
            viewholder.lilao =  convertView.findViewById(R.id.lnlao_1);
            viewholder.process =  convertView.findViewById(R.id.processbar);
            convertView.setTag(viewholder);

        }else {
            viewholder  = (ViewHolder) convertView.getTag();
        }
        Device device;
        device = Objects.get(position);
        Integer level = device.getData().get(device.getData().size() -1 ).second;
        viewholder.tv1.setText( device.getAPIkey());

        viewholder.tv2.setText("value: "+String.valueOf(level));

       viewholder.process.setProgress(level);

        if(level < 20 && level >= 0) {
            //viewholder.lilao.setBackgroundColor(this.getContext().getResources().getColor(R.color.Dangerous));
            viewholder.process.setProgressColor(this.getContext().getResources().getColor(R.color.Dangerous));
        }
        else if(level >= 20 && level < 60) {
            //viewholder.lilao.setBackgroundColor(this.getContext().getResources().getColor(R.color.medidum));
            viewholder.process.setProgressColor(this.getContext().getResources().getColor(R.color.Medidum));
        }
        else{

            //viewholder.lilao.setBackgroundColor(this.getContext().getResources().getColor(R.color.safe));
            viewholder.process.setProgressColor(this.getContext().getResources().getColor(R.color.Safe));
        }
        viewholder.lilao.setBackgroundColor(this.getContext().getResources().getColor(R.color.BackgroundItem));

        return convertView;
    }

    private class ViewHolder{
        TextView tv1;
        TextView tv2;
        LinearLayout lilao;
        RoundCornerProgressBar process;
    }
}
