package com.example.adam.pubtrans.activities;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by Adam on 17/06/2015.
 */
public class BaseActivity extends AppCompatActivity {
    public static String TAG = "BaseActivity";

    public void failure(String error, JSONObject object) {
        Log.e(TAG, error);
    }

}
