package com.example.adam.pubtrans.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.adam.pubtrans.models.NearMeResult;
import com.example.adam.pubtrans.models.Stop;
import com.example.adam.pubtrans.models.Values;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Adam on 28/06/2015.
 */
public class SharedPreferencesHelper {
    public static String SP = "sharedpreferences";
    public static String SP_STOPS_ARRAY_LIST = "stopsarraylist";
    public static String SP_ALARMS_ARRAY_LIST = "alarmsarraylist";

    public static void saveFavouriteStop(Context c, NearMeResult stop) {
        SharedPreferences sp = c.getSharedPreferences(SP, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type listOfTestObject = new TypeToken<List<NearMeResult>>(){}.getType();
        String json = sp.getString(SP_STOPS_ARRAY_LIST, "");
        ArrayList<NearMeResult> stopArrayList;
        if(json.isEmpty()) {
            stopArrayList = new ArrayList<>();
        }
        else {
            stopArrayList = gson.fromJson(json, listOfTestObject);
        }
        if(!stopArrayList.contains(stop)) {
            stopArrayList.add(stop);
            json = gson.toJson(stopArrayList);
            SharedPreferences.Editor e = sp.edit();
            e.putString(SP_STOPS_ARRAY_LIST, json);
            e.commit();
        }
    }

    public static void removeFavouriteStop(Context c,NearMeResult stop) {
        SharedPreferences sp = c.getSharedPreferences(SP, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type listOfTestObject = new TypeToken<List<NearMeResult>>(){}.getType();
        String json = sp.getString(SP_STOPS_ARRAY_LIST, "");
        ArrayList<NearMeResult> stopArrayList;
        if(json.isEmpty()) {
            return;
        }
        stopArrayList = gson.fromJson(json, listOfTestObject);
        stopArrayList.remove(stop);
        json = gson.toJson(stopArrayList);
        SharedPreferences.Editor e = sp.edit();
        e.putString(SP_STOPS_ARRAY_LIST, json);
        e.commit();
    }

    public static boolean isFavouriteStop(Context c,NearMeResult stop) {
        SharedPreferences sp = c.getSharedPreferences(SP, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type listOfTestObject = new TypeToken<List<NearMeResult>>(){}.getType();
        String json = sp.getString(SP_STOPS_ARRAY_LIST, "");
        ArrayList<NearMeResult> stopArrayList;
        if(json.isEmpty()) {
            return false;
        }
        stopArrayList = gson.fromJson(json, listOfTestObject);
        if(stopArrayList.contains(stop)) return true;
        return false;
    }

    public static ArrayList<NearMeResult> getFavouriteStops(Context c) {
        SharedPreferences sp = c.getSharedPreferences(SP, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type listOfTestObject = new TypeToken<List<NearMeResult>>(){}.getType();
        String json = sp.getString(SP_STOPS_ARRAY_LIST, "");
        ArrayList<NearMeResult> stopArrayList;
        if(json.isEmpty()) {
            return new ArrayList<>();
        }
        stopArrayList = gson.fromJson(json, listOfTestObject);
        return stopArrayList;
    }

    public static void saveAlarmJson(String v, Context c) {
        SharedPreferences sp = c.getSharedPreferences(SP, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type listOfTestObject = new TypeToken<List<String>>(){}.getType();
        String json = sp.getString(SP_ALARMS_ARRAY_LIST, "");
        ArrayList<String> stopArrayList;
        if(json.isEmpty()) {
            stopArrayList = new ArrayList<>();
        }
        else {
            stopArrayList = gson.fromJson(json, listOfTestObject);
        }
        if(!stopArrayList.contains(v)) {
            stopArrayList.add(v);
            json = gson.toJson(stopArrayList);
            SharedPreferences.Editor e = sp.edit();
            e.putString(SP_ALARMS_ARRAY_LIST, json);
            e.commit();
        }
    }

    public static void removeAlarmJson(Context c,String v) {
        SharedPreferences sp = c.getSharedPreferences(SP, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type listOfTestObject = new TypeToken<List<String>>(){}.getType();
        String json = sp.getString(SP_ALARMS_ARRAY_LIST, "");
        ArrayList<String> stopArrayList;
        if(json.isEmpty()) {
            return;
        }
        stopArrayList = gson.fromJson(json, listOfTestObject);
        stopArrayList.remove(v);
        json = gson.toJson(stopArrayList);
        SharedPreferences.Editor e = sp.edit();
        e.putString(SP_ALARMS_ARRAY_LIST, json);
        e.commit();
    }

    public static boolean isAlarmActivated(Context c,String v) {
        SharedPreferences sp = c.getSharedPreferences(SP, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type listOfTestObject = new TypeToken<List<String>>(){}.getType();
        String json = sp.getString(SP_ALARMS_ARRAY_LIST, "");
        ArrayList<String> stopArrayList;
        if(json.isEmpty()) {
            return false;
        }
        stopArrayList = gson.fromJson(json, listOfTestObject);
        if(stopArrayList.contains(v)) return true;
        return false;
    }
}
