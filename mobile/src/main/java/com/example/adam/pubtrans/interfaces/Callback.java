package com.example.adam.pubtrans.interfaces;

import org.json.JSONObject;

/**
 * Created by Adam on 31/10/2015.
 */
public interface Callback<T> {

    void success(T t);

    void failure(String error, JSONObject jsonObject);
}
