package com.example.adam.pubtrans.models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Adam on 31/05/2015.
 */
public class Stop implements Comparable<Stop>, Serializable{
    public String suburb;
    @SerializedName("transport_type")
    public String transportType;
    @SerializedName("location_name")
    public String locationName;
    @SerializedName("stop_id")
    public int stopId;
    @SerializedName("lat")
    public double latitude;
    @SerializedName("lon")
    public double longitude;
    public double distance;


    @Override
    public int compareTo(Stop other){
        // compareTo should return < 0 if this is supposed to be
        // less than other, > 0 if this is supposed to be greater than
        // other and 0 if they are supposed to be equal
        if(this.transportType.equals(other.transportType)&&this.stopId==other.stopId) return 0;
        return -1;
    }
}
