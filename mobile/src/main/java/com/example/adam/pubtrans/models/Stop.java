package com.example.adam.pubtrans.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Adam on 31/05/2015.
 */
public class Stop implements Comparable<Stop>, Serializable{
    public String suburb;
    public String transportType;
    public String locationName;
    public int stopId;
    public double latitude;
    public double longitude;
    public double distance;

    public Stop(JSONObject json) throws JSONException {
        this.suburb = json.getString("suburb");
        this.transportType = json.getString("transport_type");
        this.locationName = json.getString("location_name");
        this.stopId = json.getInt("stop_id");
        this.latitude = json.getDouble("lat");
        this.longitude = json.getDouble("lon");
        this.distance = json.getDouble("distance");
    }

    @Override
    public int compareTo(Stop other){
        // compareTo should return < 0 if this is supposed to be
        // less than other, > 0 if this is supposed to be greater than
        // other and 0 if they are supposed to be equal
        if(this.transportType.equals(other.transportType)&&this.stopId==other.stopId) return 0;
        return -1;
    }
}
