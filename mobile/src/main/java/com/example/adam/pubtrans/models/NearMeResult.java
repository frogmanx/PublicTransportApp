package com.example.adam.pubtrans.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Adam on 30/05/2015.
 */
public class NearMeResult {
    public String suburb;
    public String transportType;
    public String locationName;
    public int stopId;
    public double latitude;
    public double longitude;
    public double distance;
    public String type;

    public NearMeResult(JSONObject json) throws JSONException{
        this.type = json.getString("type");
        JSONObject result = json.getJSONObject("result");
        this.suburb = result.getString("suburb");
        this.transportType = result.getString("transport_type");
        this.locationName = result.getString("location_name");
        this.stopId = result.getInt("stop_id");
        this.latitude = result.getDouble("lat");
        this.longitude = result.getDouble("lon");
        this.distance = result.getDouble("distance");
    }

}
