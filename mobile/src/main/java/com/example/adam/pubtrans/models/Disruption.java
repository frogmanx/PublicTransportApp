package com.example.adam.pubtrans.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Adam on 31/05/2015.
 */
public class Disruption {
    public String title;
    public String url;
    public String description;
    public String publishedOn;
    public String type;
    public Disruption(JSONObject jsonObject, String type) throws JSONException{
        this.title = jsonObject.getString("title");
        this.url = jsonObject.getString("url");
        this.description = jsonObject.getString("description");
        this.publishedOn = jsonObject.getString("publishedOn");
        this.type = type;
    }
}
