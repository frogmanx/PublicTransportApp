package com.example.adam.pubtrans.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Adam on 31/10/2015.
 */
public class Result implements Comparable<Result>, Parcelable {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.suburb);
        dest.writeString(this.transportType);
        dest.writeString(this.locationName);
        dest.writeInt(this.stopId);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.distance);
    }

    public Result() {
    }

    protected Result(Parcel in) {
        this.suburb = in.readString();
        this.transportType = in.readString();
        this.locationName = in.readString();
        this.stopId = in.readInt();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.distance = in.readDouble();
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        public Result createFromParcel(Parcel source) {
            return new Result(source);
        }

        public Result[] newArray(int size) {
            return new Result[size];
        }
    };
}