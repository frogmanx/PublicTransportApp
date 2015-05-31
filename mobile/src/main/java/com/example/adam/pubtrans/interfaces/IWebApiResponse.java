package com.example.adam.pubtrans.interfaces;

import com.example.adam.pubtrans.models.NearMeResult;

import java.util.ArrayList;

/**
 * Created by Adam on 30/05/2015.
 */
public interface IWebApiResponse {
    public void nearMeResponse(ArrayList<NearMeResult> nearMeResults);
    public void nearMeResponse(String nearMeResults);
}
