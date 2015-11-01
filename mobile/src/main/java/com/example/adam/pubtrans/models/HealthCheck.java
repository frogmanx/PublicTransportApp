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

}
