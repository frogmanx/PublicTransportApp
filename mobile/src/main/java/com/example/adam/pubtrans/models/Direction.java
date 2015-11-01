package com.example.adam.pubtrans.models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Adam on 31/05/2015.
 */
public class Direction {
    @SerializedName("linedir_id")
    public int lineDirId;
    @SerializedName("direction_id")
    public int directionId;
    @SerializedName("direction_name")
    public String directionName;
    public Line line;
}
