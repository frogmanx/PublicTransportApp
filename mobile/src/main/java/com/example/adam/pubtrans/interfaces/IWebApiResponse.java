package com.example.adam.pubtrans.interfaces;

import com.example.adam.pubtrans.models.BroadNextDeparturesResult;
import com.example.adam.pubtrans.models.Disruption;
import com.example.adam.pubtrans.models.DisruptionsResult;
import com.example.adam.pubtrans.models.NearMeResult;
import com.example.adam.pubtrans.models.Stop;
import com.example.adam.pubtrans.models.Values;

import java.util.ArrayList;

/**
 * Created by Adam on 30/05/2015.
 */
public interface IWebApiResponse {
    public void nearMeResponse(ArrayList<NearMeResult> nearMeResults);
    public void broadNextDeparturesResponse(ArrayList<BroadNextDeparturesResult> broadNextDeparturesResults);
    public void disruptionsResponse(ArrayList<Disruption> disruptionsResults);
    public void stopsOnLineResponse(ArrayList<Stop> stopsOnLineResults);
    public void valuesResponse(ArrayList<Values> valuesResults);
    public void searchResponse(ArrayList<Values> valuesResults);
}
