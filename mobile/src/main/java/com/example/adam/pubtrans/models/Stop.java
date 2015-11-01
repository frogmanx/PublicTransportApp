package com.example.adam.pubtrans.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Adam on 31/05/2015.
 */
public class Stop implements Comparable<Stop>, Parcelable{
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

    public Stop() {
    }

    protected Stop(Parcel in) {
        this.suburb = in.readString();
        this.transportType = in.readString();
        this.locationName = in.readString();
        this.stopId = in.readInt();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.distance = in.readDouble();
    }

    public static final Creator<Stop> CREATOR = new Creator<Stop>() {
        public Stop createFromParcel(Parcel source) {
            return new Stop(source);
        }

        public Stop[] newArray(int size) {
            return new Stop[size];
        }
    };
}
