package com.example.adam.pubtrans.models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Adam on 31/05/2015.
 */
public class Line {
    @SerializedName("transport_type")
    public String transportType;
    @SerializedName("line_id")
    public int lineId;
    @SerializedName("line_name")
    public String lineName;
    @SerializedName("line_number")
    public String lineNumber;
}
