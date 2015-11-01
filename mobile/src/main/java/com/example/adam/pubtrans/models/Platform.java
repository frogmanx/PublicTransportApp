package com.example.adam.pubtrans.models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Adam on 31/05/2015.
 */
public class Platform {
    @SerializedName("realtime_id")
    public int realTimeId;
    public Stop stop;
    public Direction direction;

}
