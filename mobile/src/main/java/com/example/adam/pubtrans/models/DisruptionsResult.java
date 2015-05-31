package com.example.adam.pubtrans.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Adam on 31/05/2015.
 */
public class DisruptionsResult {
    public ArrayList<Disruption> disruptionArray;

    public DisruptionsResult(JSONObject jsonObject) throws JSONException{
        this.disruptionArray = new ArrayList<>();
        setDisruptionArray("general", jsonObject);
        setDisruptionArray("metro-train", jsonObject);
        setDisruptionArray("metro-tram", jsonObject);
        setDisruptionArray("metro-bus", jsonObject);
        setDisruptionArray("regional-train", jsonObject);
        setDisruptionArray("regional-coach", jsonObject);
        setDisruptionArray("regional-bus", jsonObject);
    }

    private void setDisruptionArray(String tag, JSONObject jsonObject) throws JSONException{
        JSONArray array = jsonObject.getJSONArray(tag);
        for(int i = 0; i < array.length();i++) {
            this.disruptionArray.add(new Disruption(array.getJSONObject(i), tag));
        }
    }
}
