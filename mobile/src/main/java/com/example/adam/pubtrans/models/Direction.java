package com.example.adam.pubtrans.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Adam on 31/05/2015.
 */
public class Direction {
    public int lineDirId;
    public int directionId;
    public String directionName;
    public Line line;

    public  Direction(JSONObject json) throws JSONException{
        this.lineDirId = json.getInt("linedir_id");
        this.directionId = json.getInt("direction_id");
        this.directionName = json.getString("direction_name");
        this.line = new Line(json.getJSONObject("line"));
    }


}
