package com.example.adam.pubtrans.utils;

import com.example.adam.pubtrans.models.BroadNextDeparturesResult;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Adam on 31/10/2015.
 */
public class BroadNextDeparturesDeserializer implements JsonDeserializer<ArrayList<BroadNextDeparturesResult>> {
    @Override
    public ArrayList<BroadNextDeparturesResult> deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException
    {
        // Get the "content" element from the parsed JSON
        JsonElement content = je.getAsJsonObject().get("values");

        // Deserialize it. You use a new instance of Gson to avoid infinite recursion
        // to this deserializer
        return new Gson().fromJson(content, new TypeToken<ArrayList<BroadNextDeparturesResult>>() {}.getType());

    }
}
