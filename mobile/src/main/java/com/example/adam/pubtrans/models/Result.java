package com.example.adam.pubtrans.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Adam on 31/10/2015.
 */
public class Result implements Comparable<Result> {
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
    public int compareTo(Result other){
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
        if(o instanceof Result) {
            Log.e("ResultCompare", this.transportType + " vs " + ((Result) o).transportType + " and " + this.stopId + " vs " + ((Result) o).stopId);
            return (this.transportType.equals(((Result) o).transportType) && this.stopId == ((Result) o).stopId);
        }
        return false;
    }
}