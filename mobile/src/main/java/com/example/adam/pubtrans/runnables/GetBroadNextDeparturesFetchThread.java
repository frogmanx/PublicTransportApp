package com.example.adam.pubtrans.runnables;

import android.util.Log;

import com.example.adam.pubtrans.interfaces.IWebApiResponse;
import com.example.adam.pubtrans.models.BroadNextDeparturesResult;
import com.example.adam.pubtrans.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Adam on 30/05/2015.
 */
public class GetBroadNextDeparturesFetchThread implements Runnable {
    public final static String TAG = "GetBroadNextDeparturesFetchThread";
    private String mUrl;
    private IWebApiResponse mReponseInterface;
    public GetBroadNextDeparturesFetchThread(String url, IWebApiResponse responseInterface) {
        this.mUrl = url;
        this.mReponseInterface = responseInterface;
    }

    @Override
    public void run() {
        Log.e("GetBroadNextDepartures", "started");


        try {
            String response = HttpUtils.httpGet(mUrl);
            JSONArray jsonArray = (new JSONObject(response)).getJSONArray("values");
            ArrayList<BroadNextDeparturesResult> resultArrayList = new ArrayList<>();
            for(int i = 0; i < jsonArray.length(); i++) {
                resultArrayList.add(new BroadNextDeparturesResult(jsonArray.getJSONObject(i)));
            }

            mReponseInterface.broadNextDeparturesResponse(resultArrayList);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
