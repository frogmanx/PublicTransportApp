package com.example.adam.pubtrans.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Adam on 3/06/2015.
 */
public class Values {
    public Platform platform;
    public Run run;
    public String timeTable;
    public String realTime;
    public String flags;

    public Values(JSONObject jsonObject)throws JSONException {
        this.platform = new Platform(jsonObject.getJSONObject("platform"));
        this.run = new Run(jsonObject.getJSONObject("run"));
        this.timeTable = jsonObject.getString("time_timetable_utc");
        this.realTime = jsonObject.getString("time_realtime_utc");
        this.flags = jsonObject.getString("flags");
    }
}