package com.example.adam.pubtrans.runnables;

import android.util.Log;

import com.example.adam.pubtrans.interfaces.IWebApiResponse;
import com.example.adam.pubtrans.models.HealthCheck;
import com.example.adam.pubtrans.models.Values;
import com.example.adam.pubtrans.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Adam on 17/06/2015.
 */
public class HealthCheckThread implements Runnable {
    public final static String TAG = "GetBroadNextDeparturesFetchThread";
    private String mUrl;
    public HealthCheckThread(String url) {
        this.mUrl = url;
    }

    @Override
    public void run() {
        Log.e("HealthCheckThread", "started");


        try {
            String response = HttpUtils.httpGet(mUrl);
            Log.e("HealthCheckResponse", response);
            HealthCheck healthCheck = new HealthCheck(new JSONObject(response));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}

