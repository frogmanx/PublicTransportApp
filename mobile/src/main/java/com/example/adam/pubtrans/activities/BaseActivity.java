package com.example.adam.pubtrans.activities;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import com.example.adam.pubtrans.interfaces.IResults;
import com.example.adam.pubtrans.interfaces.IWebApiResponse;
import com.example.adam.pubtrans.models.BroadNextDeparturesResult;
import com.example.adam.pubtrans.models.Disruption;
import com.example.adam.pubtrans.models.NearMeResult;
import com.example.adam.pubtrans.models.Stop;
import com.example.adam.pubtrans.models.Values;

import java.util.ArrayList;

/**
 * Created by Adam on 17/06/2015.
 */
public class BaseActivity extends FragmentActivity implements IWebApiResponse {

    public void nearMeResponse(final ArrayList<NearMeResult> nearMeResults) {

    }

    public void broadNextDeparturesResponse(final ArrayList<BroadNextDeparturesResult> broadNextDeparturesResponse) {

    }

    public void disruptionsResponse(final ArrayList<Disruption>  disruptionsResults) {

    }

    public void stopsOnLineResponse(final ArrayList<Stop>  stopResults) {

    }

    public void valuesResponse(ArrayList<Values> valuesResults) {

    }

    public void searchResponse(ArrayList<Values> valuesResults){

    }
}
