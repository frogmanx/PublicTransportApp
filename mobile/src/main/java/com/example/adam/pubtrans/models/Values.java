package com.example.adam.pubtrans.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Adam on 3/06/2015.
 */
public class Values implements Parcelable{
    public Platform platform;
    public Run run;
    @SerializedName("time_timetable_utc")
    public String timeTable;
    @SerializedName("time_realtime_utc")
    public String realTime;
    public String flags;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.platform, flags);
        dest.writeParcelable(this.run, flags);
        dest.writeString(this.timeTable);
        dest.writeString(this.realTime);
        dest.writeString(this.flags);
    }

    public Values() {
    }

    protected Values(Parcel in) {
        this.platform = in.readParcelable(Platform.class.getClassLoader());
        this.run = in.readParcelable(Run.class.getClassLoader());
        this.timeTable = in.readString();
        this.realTime = in.readString();
        this.flags = in.readString();
    }

    public static final Creator<Values> CREATOR = new Creator<Values>() {
        public Values createFromParcel(Parcel source) {
            return new Values(source);
        }

        public Values[] newArray(int size) {
            return new Values[size];
        }
    };
}