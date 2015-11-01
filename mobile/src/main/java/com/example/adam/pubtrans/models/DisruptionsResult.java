package com.example.adam.pubtrans.models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Adam on 31/05/2015.
 */
public class DisruptionsResult {
    public ArrayList<Disruption> general;
    @SerializedName("metro-train")
    public ArrayList<Disruption> metroTrain;
    @SerializedName("metro-tram")
    public ArrayList<Disruption> metroTram;
    @SerializedName("metro-bus")
    public ArrayList<Disruption> metroBus;
    @SerializedName("regional-train")
    public ArrayList<Disruption> regionalTrain;
    @SerializedName("regional-coach")
    public ArrayList<Disruption> regionalCoach;
    @SerializedName("regional-bus")
    public ArrayList<Disruption> regionalBus;


}
