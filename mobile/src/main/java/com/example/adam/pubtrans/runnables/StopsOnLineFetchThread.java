package com.example.adam.pubtrans.runnables;

import android.util.Log;

import com.example.adam.pubtrans.interfaces.IWebApiResponse;
import com.example.adam.pubtrans.models.DisruptionsResult;
import com.example.adam.pubtrans.models.NearMeResult;
import com.example.adam.pubtrans.models.Stop;
import com.example.adam.pubtrans.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Adam on 31/05/2015.
 */
public class StopsOnLineFetchThread implements Runnable {
    public final static String TAG = "StopsOnLineFetchThread";
    private String mUrl;
    private IWebApiResponse mReponseInterface;
    public StopsOnLineFetchThread(String url, IWebApiResponse responseInterface) {
        this.mUrl = url;
        this.mReponseInterface = responseInterface;
    }

    @Override
    public void run() {
        Log.e("StopsOnLineFetchThread", "started");


        try {
            String response = HttpUtils.httpGet(mUrl);
            Log.e(TAG, response);
            JSONArray jsonArray = new JSONArray(response);
            ArrayList<Stop> resultArrayList = new ArrayList<>();
            for(int i = 0; i < jsonArray.length(); i++) {
                resultArrayList.add(new Stop(jsonArray.getJSONObject(i)));
            }

            mReponseInterface.stopsOnLineResponse(resultArrayList);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}