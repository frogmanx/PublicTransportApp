package com.example.adam.pubtrans.models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Adam on 3/06/2015.
 */
public class Values implements Serializable{
    public Platform platform;
    public Run run;
    @SerializedName("time_timetable_utc")
    public String timeTable;
    @SerializedName("time_realtime_utc")
    public String realTime;
    public String flags;
}