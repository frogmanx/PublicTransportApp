package com.example.adam.pubtrans.interfaces;

import android.util.Log;

import com.example.adam.pubtrans.utils.BroadNextDeparturesDeserializer;
import com.example.adam.pubtrans.models.BroadNextDeparturesResult;
import com.example.adam.pubtrans.models.Values;
import com.example.adam.pubtrans.utils.ValuesDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Adam on 31/10/2015.
 */
public class WebApiGenericResponse<T> implements WebApiResponse {

    public static final String TAG = "IWebAPIGenericResponse";

    protected Callback<T> callback;
    Type type;

    public WebApiGenericResponse(Callback<T> callback, Type type) {
        this.callback = callback;
        //this.type = new TypeToken<T>(){}.getType(); //Generics casting issues here.
        this.type = type;
    }


    /** Successful HTTP response. */
    public void success(Object json) throws JSONException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(new TypeToken<ArrayList<BroadNextDeparturesResult>>() {}.getType(), new BroadNextDeparturesDeserializer())
                .registerTypeAdapter(new TypeToken<ArrayList<Values>>() {}.getType(), new ValuesDeserializer())
                .create();
        if(callback!=null) {
            T result = gson.fromJson(json.toString(), type);
            callback.success(result);
        }
        else {
            Log.i(TAG, "Must pass valid callback into " + TAG + " constructor.");
        }
    }

    /**
     * Unsuccessful HTTP response due to network failure, non-2XX status code, or unexpected
     * exception.
     */
    public void failure(String error, JSONObject jsonObject) {
        if(callback!=null) {
            callback.failure(error, jsonObject);

        }
        else {
            Log.i(TAG, "Must pass valid callback into " + TAG + "  constructor.");
        }
    }
}
