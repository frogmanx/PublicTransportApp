package com.example.adam.pubtrans.runnables;

import android.util.Log;

import com.example.adam.pubtrans.interfaces.IWebApiResponse;
import com.example.adam.pubtrans.models.NearMeResult;
import com.example.adam.pubtrans.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Adam on 30/05/2015.
 */
public class GetNearMeFetchThread implements Runnable {
    private String mUrl;
    private IWebApiResponse mReponseInterface;
    public GetNearMeFetchThread(String url, IWebApiResponse getNearMeInterface) {
        this.mUrl = url;
        this.mReponseInterface = getNearMeInterface;
    }

    @Override
    public void run() {
        Log.e("CryptsyFetchTread", "started");


        try {
            //JSONObject result = new JSONObject(HttpUtils.httpGet(mUrl));
            //Log.e("response", result.toString());
            JSONArray jsonArray = new JSONArray(HttpUtils.httpGet(mUrl));
            ArrayList<NearMeResult> resultArrayList = new ArrayList<>();
            for(int i = 0; i < jsonArray.length(); i++) {
                resultArrayList.add(new NearMeResult(jsonArray.getJSONObject(i)));
            }

            mReponseInterface.nearMeResponse(resultArrayList);

            //info = new BTCegetInfo(result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
