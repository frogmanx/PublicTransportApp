package com.example.adam.pubtrans.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Adam on 31/05/2015.
 */
public class Platform implements Parcelable{
    @SerializedName("realtime_id")
    public int realTimeId;
    public Stop stop;
    public Direction direction;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.realTimeId);
        dest.writeParcelable(this.stop, 0);
        dest.writeParcelable(this.direction, 0);
    }

    public Platform() {
    }

    protected Platform(Parcel in) {
        this.realTimeId = in.readInt();
        this.stop = in.readParcelable(Stop.class.getClassLoader());
        this.direction = in.readParcelable(Direction.class.getClassLoader());
    }

    public static final Creator<Platform> CREATOR = new Creator<Platform>() {
        public Platform createFromParcel(Parcel source) {
            return new Platform(source);
        }

        public Platform[] newArray(int size) {
            return new Platform[size];
        }
    };
}
