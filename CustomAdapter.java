package com.example.hoang.datafromserver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoang on 10/09/2017.
 */

public class CustomAdapter extends ArrayAdapter<Device> {
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
            viewholder.tv2 = (TextView) convertView.findViewById(R.id.tv_1);
            convertView.setTag(viewholder);

        }else {
            viewholder  = (ViewHolder) convertView.getTag();
        }
        Device device;
        device = Objects.get(position);
        viewholder.tv2.setText((CharSequence) device.getAPIkey());
        return convertView;
    }

    public class ViewHolder{
        TextView tv2;
    }
}
