package com.example.adam.pubtrans.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class ISO8601 {
    /** Transform Calendar to ISO 8601 string. */
    public static String fromCalendar(final Calendar calendar) {
        Date date = calendar.getTime();
        String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .format(date);
        return formatted.substring(0, 22) + ":" + formatted.substring(22);
    }

    /** Get current date and time formatted as ISO 8601 string. */
    public static String now() {
        return fromCalendar(GregorianCalendar.getInstance());
    }

    public static String convertToTimeContext(long timeDistance) {
        String endString = "";
        if(timeDistance < 0) {
            endString = " behind schedule";
            timeDistance = Math.abs(timeDistance);
        }
        else {
            endString = " from now";
        }
        long nextTimeDistance = timeDistance / 1000; //second
        if(nextTimeDistance == 0) return Long.toString(timeDistance) + "ms" + endString;
        timeDistance = nextTimeDistance;
        nextTimeDistance = timeDistance / 60; //minute
        if(nextTimeDistance == 0) return Long.toString(timeDistance) + " seconds" + endString;
        timeDistance = nextTimeDistance;
        nextTimeDistance = timeDistance / 60; //hour
        if(nextTimeDistance == 0) return Long.toString(timeDistance) + " minutes" + endString;
        timeDistance = nextTimeDistance;
        nextTimeDistance = timeDistance / 60; //days
        if(nextTimeDistance == 0) return Long.toString(timeDistance) + " hours" + endString;
        timeDistance = nextTimeDistance;
        nextTimeDistance = timeDistance / 24; //days
        if(nextTimeDistance == 0) return Long.toString(timeDistance) + " days" + endString;
        return Long.toString(timeDistance);
    }

    /** Transform ISO 8601 string to Calendar. */
    public static Calendar toCalendar(final String iso8601string)
            throws ParseException {
        Calendar calendar = GregorianCalendar.getInstance();
        String s = iso8601string.replace("Z", "+00:00");
        try {
            s = s.substring(0, 22) + s.substring(23);  // to get rid of the ":"
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Invalid length", 0);
        }
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(s);
        calendar.setTime(date);
        return calendar;
    }
}