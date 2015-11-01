package com.example.adam.pubtrans.models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Adam on 30/05/2015.
 */
public class BroadNextDeparturesResult {
    public Platform platform;
    public Run run;
    @SerializedName("time_timetable_utc")
    public String timeTimeTableUTC;
    @SerializedName("time_realtime_utc")
    public String timeRealTimeUTC;
    @SerializedName("flags")
    public String flags;

}
