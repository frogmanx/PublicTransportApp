package com.example.adam.pubtrans.utils;

import android.net.Uri;
import android.util.Log;

import com.example.adam.pubtrans.interfaces.Callback;
import com.example.adam.pubtrans.interfaces.WebApiGenericResponse;
import com.example.adam.pubtrans.models.BroadNextDeparturesResult;
import com.example.adam.pubtrans.models.DisruptionsResult;
import com.example.adam.pubtrans.models.HealthCheck;
import com.example.adam.pubtrans.models.NearMeResult;
import com.example.adam.pubtrans.models.Stop;
import com.example.adam.pubtrans.models.Values;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.security.Key;
import java.util.ArrayList;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Adam on 30/05/2015.
 */
public class WebApi extends WebApiBase{


    public static void getNearMe(LatLng myLocation, Callback<ArrayList<NearMeResult>> callback) {
        newCall("/v2/nearme/latitude/" + myLocation.latitude + "/longitude/" + myLocation.longitude,
                new WebApiGenericResponse<>(callback,
                        new TypeToken<ArrayList<NearMeResult>>() {}.getType()));
    }

    public static void getSearch(String searchValue, Callback<ArrayList<NearMeResult>> callback) {
        searchValue = Uri.encode(searchValue);
        newCall("/v2/search/" + searchValue,
                new WebApiGenericResponse<>(callback,
                        new TypeToken<ArrayList<NearMeResult>>() {}.getType()));
    }

    public static void getDisruptions(Callback<DisruptionsResult> callback) throws Exception{
        newCall("/v2/disruptions/modes/general,metro-train,metro-tram,metro-bus,regional-train,regional-bus,regional-coach",
                new WebApiGenericResponse<>(callback,
                        new TypeToken<DisruptionsResult>() {
                        }.getType()));
    }

    public static void getStopsOnALine(String transportType, int lineId, Callback<ArrayList<Stop>> callback) throws Exception{
        int mode = getModeId(transportType);
        newCall("/v2/mode/" + Integer.toString(mode) + "/line/" + Integer.toString(lineId) +  "/stops-for-line",
                new WebApiGenericResponse<>(callback,
                        new TypeToken<ArrayList<Stop>>() {
                        }.getType()));
    }


    public static void getBroadNextDepatures(String transportType, int stopId, int limit, Callback<ArrayList<BroadNextDeparturesResult>> callback) throws Exception{
        int mode = getModeId(transportType);
        newCall("/v2/mode/" + Integer.toString(mode) + "/stop/" + Integer.toString(stopId) + "/departures/by-destination/limit/" + Integer.toString(limit),
                new WebApiGenericResponse<>(callback,
                        new TypeToken<ArrayList<BroadNextDeparturesResult>>() {
                        }.getType()));
    }

    public static void getSpecificNextDepatures(String transportType, int lineId, int stopId, int directionId, int limit, Callback<ArrayList<BroadNextDeparturesResult>> callback) throws Exception{
        int mode = getModeId(transportType);
        newCall("/v2/mode/" + Integer.toString(mode) + "/line/" + Integer.toString(lineId) + "/stop/" + Integer.toString(stopId) +"/directionid/" + Integer.toString(directionId) + "/departures/all/limit/" + Integer.toString(limit),
                new WebApiGenericResponse<>(callback,
                        new TypeToken<ArrayList<BroadNextDeparturesResult>>() {
                        }.getType()));
    }

    public static void getStoppingPattern(String transportType, int runId, int stopId, Callback<ArrayList<Values>> callback) throws Exception{
        int mode = getModeId(transportType);
        newCall("/v2/mode/" + Integer.toString(mode) + "/run/" + Integer.toString(runId) + "/stop/" + Integer.toString(stopId) + "/stopping-pattern",
                new WebApiGenericResponse<>(callback,
                        new TypeToken<ArrayList<Values>>() {
                        }.getType()));
    }
    public static void getHealthCheckStatus(Callback<HealthCheck> callback) throws Exception{
        newCall("/v2/healthcheck",
                new WebApiGenericResponse<>(callback,
                        new TypeToken<HealthCheck>() {
                        }.getType()));
    }

    public static int getModeId(String transportType) {
        int mode;
        switch(transportType) {
            case PTVConstants.TRAIN_TYPE: mode = 0; break;
            case PTVConstants.TRAM_TYPE: mode = 1; break;
            case PTVConstants.BUS_TYPE: mode = 2; break;
            case PTVConstants.NIGHTRIDER_TYPE: mode = 4; break;
            default: mode = 3; break;
        }
        return mode;
    }

}
