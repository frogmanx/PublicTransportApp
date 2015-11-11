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
        SimpleDateFormat sdf= new SimpleDateFormat("HH:mm a", Locale.US);
        Log.e("ValuesHolder", utcTime);
        Date now = new Date();
        DateTime dateTime = new DateTime( utcTime, DateTimeZone.getDefault() );
        if(failTime) {
            dateTime = dateTime.plusDays(1);
        }
        Date scheduledTime = dateTime.toDate();
        long difference = scheduledTime.getTime()-now.getTime();
        return sdf.format(scheduledTime) + " (" + ISO8601.convertToTimeContext(difference) + ")";

    }

    public static float getAlphaFromTime(String utcTime, double threshold) {
        double x = convertToMSAway(utcTime);
        double y;
        if (x < -threshold || x > threshold) y = 0;
        else y = ((-Math.abs(-x))+threshold) / threshold;
        return (float) y;
    }


    public static long convertToMSAway(String utcTime) {
        DateTime utcDateTime = new DateTime( utcTime, DateTimeZone.getDefault() );
        Date now = new Date();
        return utcDateTime.toDate().getTime()-now.getTime();
    }

    public static String convertToContext(String utcTime, String comparisonTime, boolean failTime) {
        if(utcTime.contentEquals("null")||comparisonTime.contentEquals("null")){
            return "";
        }
        SimpleDateFormat sdf= new SimpleDateFormat("HH:mm a", Locale.US);
        Log.e("ValuesHolder", utcTime);
        Date now = new Date();
        DateTime dateTime = new DateTime( utcTime, DateTimeZone.getDefault() );
        DateTime comparisonDateTime = new DateTime( comparisonTime, DateTimeZone.getDefault() );
        Date scheduledTime = dateTime.toDate();
        Date comparisionDate  = comparisonDateTime.toDate();
        long difference = scheduledTime.getTime()-comparisionDate.getTime();
        return sdf.format(scheduledTime) + " (" + ISO8601.convertToTimeContext(difference) + ")";

    }

    public static Date convertToDate(String utcTime) {
        DateTime dateTime = new DateTime( utcTime, DateTimeZone.getDefault() );
        return dateTime.toDate();
    }

}
