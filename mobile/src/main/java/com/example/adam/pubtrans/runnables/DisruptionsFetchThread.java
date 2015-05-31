package com.example.adam.pubtrans.runnables;

import android.util.Log;

import com.example.adam.pubtrans.interfaces.IWebApiResponse;
import com.example.adam.pubtrans.models.BroadNextDeparturesResult;
import com.example.adam.pubtrans.models.DisruptionsResult;
import com.example.adam.pubtrans.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Adam on 31/05/2015.
 */
public class DisruptionsFetchThread implements Runnable {
    public final static String TAG = "GetBroadNextDeparturesFetchThread";
    private String mUrl;
    private IWebApiResponse mReponseInterface;
    public DisruptionsFetchThread(String url, IWebApiResponse responseInterface) {
        this.mUrl = url;
        this.mReponseInterface = responseInterface;
    }

    @Override
    public void run() {
        Log.e("GetBroadNextDepartures", "started");


        try {
            String response = HttpUtils.httpGet(mUrl);
            DisruptionsResult disruptionsResults = new DisruptionsResult(new JSONObject(response));
            mReponseInterface.disruptionsResponse(disruptionsResults.disruptionArray);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}