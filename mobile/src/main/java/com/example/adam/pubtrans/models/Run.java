package com.example.adam.pubtrans.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Adam on 31/05/2015.
 */
public class Run implements Parcelable{
    @SerializedName("transport_type")
    public String transportType;
    @SerializedName("run_id")
    public int runId;
    @SerializedName("num_skipped")
    public int numSkipped;
    @SerializedName("destination_id")
    public int destinationId;
    @SerializedName("destination_name")
    public String destinationName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.transportType);
        dest.writeInt(this.runId);
        dest.writeInt(this.numSkipped);
        dest.writeInt(this.destinationId);
        dest.writeString(this.destinationName);
    }

    public Run() {
    }

    protected Run(Parcel in) {
        this.transportType = in.readString();
        this.runId = in.readInt();
        this.numSkipped = in.readInt();
        this.destinationId = in.readInt();
        this.destinationName = in.readString();
    }

    public static final Creator<Run> CREATOR = new Creator<Run>() {
        public Run createFromParcel(Parcel source) {
            return new Run(source);
        }

        public Run[] newArray(int size) {
            return new Run[size];
        }
    };
}
