package com.example.hoang.app;


import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by hoang on 25/09/2017.
 */

public class ProcessingTime {
    private DateFormat format;
    private DateFormat formatDayOfMonth;
    public ProcessingTime(DateFormat format,DateFormat dayofmonth) {
        this.format = format;
        this.formatDayOfMonth = dayofmonth;
    }

    ProcessingTime(){}

    public DateFormat getFormat() {
        return format;
    }

    void setFormat(DateFormat format) {
        this.format = format;
    }

    public DateFormat getFormatDayOfMonth() {
        return formatDayOfMonth;
    }

    public void setFormatDayOfMonth(DateFormat format_dayofmonth) {
        this.formatDayOfMonth = format_dayofmonth;
    }

    GregorianCalendar getTime(String strTime)
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

    public int distanceBetweenTwoTime(GregorianCalendar time1,GregorianCalendar time2,Long timeUnit)
    {
        int result;
        long distance;
        GregorianCalendar cTime = new GregorianCalendar();
        cTime.setTime(time2.getTime());
        cTime.set(Calendar.MINUTE,time1.get(Calendar.MINUTE));
        cTime.set(Calendar.SECOND,time1.get(Calendar.SECOND));
        distance = time1.getTimeInMillis() - cTime.getTimeInMillis();
        result = (int) (distance/timeUnit);
        return  result;
    }

    ArrayList<GregorianCalendar> getSevenDay(GregorianCalendar toDay){
        ArrayList<GregorianCalendar> thisWeek = new ArrayList<>();
        long millis = 86400000;
        for(int i = 6; i >= 0; i--) {
            Date date = new Date(toDay.getTimeInMillis() - i * millis);
            GregorianCalendar aDay = new GregorianCalendar();
            aDay.setTime(date);
            thisWeek.add(aDay);
        }
        return thisWeek;
    }
}
