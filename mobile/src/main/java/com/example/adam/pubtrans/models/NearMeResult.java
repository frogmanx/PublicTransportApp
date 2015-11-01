package com.example.adam.pubtrans.models;

import android.util.Log;

import com.example.adam.pubtrans.utils.PTVConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Adam on 30/05/2015.
 */
public class NearMeResult implements  Serializable, Comparable<NearMeResult> {

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


}
