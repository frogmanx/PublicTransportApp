package com.example.adam.pubtrans.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.adam.pubtrans.utils.PTVConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Adam on 30/05/2015.
 */
public class NearMeResult implements  Comparable<NearMeResult>, Parcelable {

    public String type;
    public Result result;

    public NearMeResult(String locationName) {
        this.type = PTVConstants.HEADER_TYPE;
        this.result = new Result();
        this.result.locationName = locationName;
    }

    @Override
    public int compareTo(NearMeResult other){
        return (result.compareTo(other.result));
    }


    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(o instanceof NearMeResult) {
            return (this.result.equals(((NearMeResult) o).result));
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeParcelable(this.result, 0);
    }

    protected NearMeResult(Parcel in) {
        this.type = in.readString();
        this.result = in.readParcelable(Result.class.getClassLoader());
    }

    public static final Creator<NearMeResult> CREATOR = new Creator<NearMeResult>() {
        public NearMeResult createFromParcel(Parcel source) {
            return new NearMeResult(source);
        }

        public NearMeResult[] newArray(int size) {
            return new NearMeResult[size];
        }
    };
}
