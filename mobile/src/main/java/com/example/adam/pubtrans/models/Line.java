package com.example.adam.pubtrans.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Adam on 31/05/2015.
 */
public class Line implements Parcelable{
    @SerializedName("transport_type")
    public String transportType;
    @SerializedName("line_id")
    public int lineId;
    @SerializedName("line_name")
    public String lineName;
    @SerializedName("line_number")
    public String lineNumber;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.transportType);
        dest.writeInt(this.lineId);
        dest.writeString(this.lineName);
        dest.writeString(this.lineNumber);
    }

    public Line() {
    }

    protected Line(Parcel in) {
        this.transportType = in.readString();
        this.lineId = in.readInt();
        this.lineName = in.readString();
        this.lineNumber = in.readString();
    }

    public static final Creator<Line> CREATOR = new Creator<Line>() {
        public Line createFromParcel(Parcel source) {
            return new Line(source);
        }

        public Line[] newArray(int size) {
            return new Line[size];
        }
    };
}
