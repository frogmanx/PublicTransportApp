package com.example.adam.pubtrans.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Adam on 30/05/2015.
 */
public class HealthCheck {
    boolean securityTokenOK;
    boolean clientClockOK;
    boolean memcacheOK;
    boolean databaseOK;

    public HealthCheck(JSONObject json) throws JSONException {
        this.securityTokenOK = json.getBoolean("securityTokenOK");
        this.clientClockOK = json.getBoolean("clientClockOK");
        this.memcacheOK = json.getBoolean("memcacheOK");
        this.databaseOK = json.getBoolean("databaseOK");
    }
}
