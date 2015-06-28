package com.example.adam.pubtrans.models;

import android.util.Log;

import com.example.adam.pubtrans.utils.PTVConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Adam on 30/05/2015.
 */
public class NearMeResult implements Comparable<NearMeResult>, Serializable {
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
    public NearMeResult(String locationName) {
        this.type = PTVConstants.HEADER_TYPE;
        this.locationName = locationName;
    }

    @Override
    public int compareTo(NearMeResult other){
        // compareTo should return < 0 if this is supposed to be
        // less than other, > 0 if this is supposed to be greater than
        // other and 0 if they are supposed to be equal
        if(this.transportType==null) {
            return -1;
        }
        if(this.transportType.equals(other.transportType)&&this.stopId==other.stopId) return 0;
        return -1;
    }


    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(this.transportType==null) return false;
        if(o instanceof NearMeResult) {
            Log.e("NearMeCompare", this.transportType + " vs " + ((NearMeResult) o).transportType + " and " + this.stopId + " vs " + ((NearMeResult) o).stopId);
            return (this.transportType.equals(((NearMeResult) o).transportType) && this.stopId == ((NearMeResult) o).stopId);
        }
        return false;
    }

}
