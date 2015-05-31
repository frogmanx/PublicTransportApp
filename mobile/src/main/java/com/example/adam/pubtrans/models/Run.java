package com.example.adam.pubtrans.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Adam on 31/05/2015.
 */
public class Run {
    public String transportType;
    public int runId;
    public int numSkipped;
    public int destinationId;
    public String destinationName;
    public Run(JSONObject jsonObject) throws JSONException {
        this.transportType = jsonObject.getString("transport_type");
        this.runId = jsonObject.getInt("run_id");
        this.numSkipped = jsonObject.getInt("num_skipped");
        this.destinationId = jsonObject.getInt("destination_id");
        this.destinationName = jsonObject.getString("destination_name");
    }
}
