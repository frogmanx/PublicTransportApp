package com.example.adam.pubtrans.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Adam on 31/10/2015.
 */
public interface WebApiResponse {

    void success(Object json) throws JSONException;

    void failure(String error, JSONObject jsonObject);

}

