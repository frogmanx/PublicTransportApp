package com.example.adam.pubtrans.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Adam on 31/05/2015.
 */
public class Line {
    public String transportType;
    public int lineId;
    public String lineName;
    public String lineNumber;
    public Line(JSONObject jsonObject) throws JSONException {
        this.transportType = jsonObject.getString("transport_type");
        this.lineId = jsonObject.getInt("line_id");
        this.lineName = jsonObject.getString("line_name");
        this.lineNumber = jsonObject.getString("line_number");
    }
}
