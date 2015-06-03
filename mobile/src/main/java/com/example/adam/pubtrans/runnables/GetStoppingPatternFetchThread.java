package com.example.adam.pubtrans.runnables;

import android.util.Log;

import com.example.adam.pubtrans.interfaces.IWebApiResponse;
import com.example.adam.pubtrans.models.BroadNextDeparturesResult;
import com.example.adam.pubtrans.models.Values;
import com.example.adam.pubtrans.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Adam on 4/06/2015.
 */
public class GetStoppingPatternFetchThread implements Runnable {
    public final static String TAG = "GetBroadNextDeparturesFetchThread";
    private String mUrl;
    private IWebApiResponse mReponseInterface;
    public GetStoppingPatternFetchThread(String url, IWebApiResponse responseInterface) {
        this.mUrl = url;
        this.mReponseInterface = responseInterface;
    }

    @Override
    public void run() {
        Log.e("GetStoppingPatternFetchThread", "started");


        try {
            String response = HttpUtils.httpGet(mUrl);
            Log.e("GetStoppingPattern", response);
            JSONArray jsonArray = (new JSONObject(response)).getJSONArray("values");
            ArrayList<Values> resultArrayList = new ArrayList<>();
            for(int i = 0; i < jsonArray.length(); i++) {
                resultArrayList.add(new Values(jsonArray.getJSONObject(i)));
            }

            mReponseInterface.valuesResponse(resultArrayList);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
