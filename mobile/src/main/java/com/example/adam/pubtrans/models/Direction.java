package com.example.adam.pubtrans.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Adam on 31/05/2015.
 */
public class Direction implements Parcelable{
    @SerializedName("linedir_id")
    public int lineDirId;
    @SerializedName("direction_id")
    public int directionId;
    @SerializedName("direction_name")
    public String directionName;
    public Line line;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.lineDirId);
        dest.writeInt(this.directionId);
        dest.writeString(this.directionName);
        dest.writeParcelable(this.line, flags);
    }

    public Direction() {
    }

    protected Direction(Parcel in) {
        this.lineDirId = in.readInt();
        this.directionId = in.readInt();
        this.directionName = in.readString();
        this.line = in.readParcelable(Line.class.getClassLoader());
    }

    public static final Creator<Direction> CREATOR = new Creator<Direction>() {
        public Direction createFromParcel(Parcel source) {
            return new Direction(source);
        }

        public Direction[] newArray(int size) {
            return new Direction[size];
        }
    };
}
