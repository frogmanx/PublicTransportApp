package com.example.adam.pubtrans.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Adam on 31/05/2015.
 */
public class Stop {
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
}
