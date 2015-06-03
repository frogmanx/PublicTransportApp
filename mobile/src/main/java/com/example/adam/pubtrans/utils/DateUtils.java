package com.example.adam.pubtrans.utils;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Adam on 4/06/2015.
 */
public class DateUtils {
    public static String convertToContext(String utcTime, boolean failTime) {
        if(utcTime.contentEquals("null")){
            return "";
        }
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz", Locale.US);
        Log.e("ValuesHolder", utcTime);
        Date now = new Date();
        DateTime dateTime = new DateTime( utcTime, DateTimeZone.getDefault() );
        if(failTime) {
            dateTime = dateTime.plusDays(1);
        }
        Date scheduledTime = dateTime.toDate();
        sdf.format(scheduledTime);
        long difference = scheduledTime.getTime()-now.getTime();
        return sdf.format(scheduledTime) + " (" + ISO8601.convertToTimeContext(difference) + ")";

    }
}
