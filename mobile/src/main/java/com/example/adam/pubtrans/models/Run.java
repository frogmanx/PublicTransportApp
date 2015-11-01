package com.example.adam.pubtrans.models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Adam on 31/05/2015.
 */
public class Run {
    @SerializedName("transport_type")
    public String transportType;
    @SerializedName("run_id")
    public int runId;
    @SerializedName("num_skipped")
    public int numSkipped;
    @SerializedName("destination_id")
    public int destinationId;
    @SerializedName("destination_name")
    public String destinationName;
}
