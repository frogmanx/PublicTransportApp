package com.example.adam.pubtrans.interfaces;

import com.example.adam.pubtrans.models.Disruption;
import com.example.adam.pubtrans.models.NearMeResult;
import com.example.adam.pubtrans.models.Values;

import java.util.ArrayList;

/**
 * Created by Adam on 17/06/2015.
 */
public interface IPubActivity {
    ArrayList<NearMeResult> getNearMeResults();
    ArrayList<Disruption> getDisruptionsResults();
    ArrayList<Values> getAlarms();
}
