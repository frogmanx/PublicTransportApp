package com.example.adam.pubtrans.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Adam on 30/05/2015.
 */
public class BroadNextDeparturesResult {
    public Platform platform;
    public Run run;
    public String timeTimeTableUTC;
    public String timeRealTimeUTC;
    public String flags;

    public BroadNextDeparturesResult(JSONObject jsonObject)throws JSONException{
        this.platform = new Platform(jsonObject.getJSONObject("platform"));
        this.run = new Run(jsonObject.getJSONObject("run"));
        this.timeTimeTableUTC = jsonObject.getString("time_timetable_utc");
        this.timeRealTimeUTC = jsonObject.getString("time_realtime_utc");
        this.flags = jsonObject.getString("flags");
    }
}
