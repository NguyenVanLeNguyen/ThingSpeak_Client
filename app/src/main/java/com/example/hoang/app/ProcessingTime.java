package com.example.hoang.app;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by hoang on 25/09/2017.
 */

public class ProcessingTime {
    private DateFormat format;

    public ProcessingTime(DateFormat format) {
        this.format = format;
    }

    public ProcessingTime(){}

    public GregorianCalendar getTime(String strTime)
    {
        GregorianCalendar time = null;
        try {
            Date date = format.parse(strTime);
            time = new GregorianCalendar();
            time.setTime(date);


        } catch (ParseException e) {
            e.printStackTrace();

        }
        return time;
    }

    public int DistanceBetweenTwoTime(GregorianCalendar time1,GregorianCalendar time2)
    {
        int result = 0 ;
        long distance;
        
        distance = time1.getTimeInMillis() - time2.getTimeInMillis();
        result = (int) (distance/3600000);
        return  result;
    }
}
