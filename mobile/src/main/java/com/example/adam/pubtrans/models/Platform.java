package com.example.adam.pubtrans.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Adam on 31/05/2015.
 */
public class Platform {
    public int realTimeId;
    public Stop stop;
    public Direction direction;

    public Platform(JSONObject jsonObject) throws JSONException {
        this.realTimeId = jsonObject.getInt("realtime_id");
        this.stop = new Stop(jsonObject.getJSONObject("stop"));
        this.direction = new Direction(jsonObject.getJSONObject("direction"));
    }
}
