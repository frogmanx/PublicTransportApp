package com.example.adam.pubtrans.runnables;

import android.util.Log;

import com.example.adam.pubtrans.interfaces.IWebApiResponse;
import com.example.adam.pubtrans.models.NearMeResult;
import com.example.adam.pubtrans.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Adam on 31/05/2015.
 */
public class SearchFetchThread implements Runnable {
    private String mUrl;
    private IWebApiResponse mReponseInterface;
    public SearchFetchThread(String url, IWebApiResponse searchInterface) {
        this.mUrl = url;
        this.mReponseInterface = searchInterface;
    }

    @Override
    public void run() {
        Log.e("GetNearMeFetchThread", "started");


        try {
            //JSONObject result = new JSONObject(HttpUtils.httpGet(mUrl));
            //Log.e("response", result.toString());
            JSONArray jsonArray = new JSONArray(HttpUtils.httpGet(mUrl));
            ArrayList<NearMeResult> resultArrayList = new ArrayList<>();
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.getString("type").contentEquals("stop")) {
                    resultArrayList.add(new NearMeResult(jsonObject));
                }
            }

            mReponseInterface.nearMeResponse(resultArrayList);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
