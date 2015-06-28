package com.example.adam.pubtrans.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.adam.pubtrans.models.NearMeResult;
import com.example.adam.pubtrans.models.Stop;
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
}
