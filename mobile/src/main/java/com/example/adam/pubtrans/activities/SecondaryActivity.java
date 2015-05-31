package com.example.adam.pubtrans.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.example.adam.pubtrans.models.Disruption;
import com.example.adam.pubtrans.models.DisruptionsResult;
import com.example.adam.pubtrans.models.Stop;
import com.example.adam.pubtrans.utils.PTVConstants;
import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.interfaces.IResults;
import com.example.adam.pubtrans.interfaces.IWebApiResponse;
import com.example.adam.pubtrans.models.BroadNextDeparturesResult;
import com.example.adam.pubtrans.models.NearMeResult;
import com.example.adam.pubtrans.utils.WebApi;

import java.util.ArrayList;

/**
 * Created by Adam on 31/05/2015.
 */
public class SecondaryActivity extends FragmentActivity implements IWebApiResponse {
    FragmentManager fragmentManager;
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondary_activity);



        Bundle bundle = getIntent().getExtras();
        try {
            WebApi.getBroadNextDepatures(bundle.getString(PTVConstants.TRANSPORT_TYPE), bundle.getInt(PTVConstants.STOP_ID),0,this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        getActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentManager = getSupportFragmentManager();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.secondary_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            Fragment fragment = fragmentManager.findFragmentById(R.id.fragment);
            if(fragment!=null) {
                ((IResults) fragment).refresh();
            }
            Bundle bundle = getIntent().getExtras();
            try {
                WebApi.getBroadNextDepatures(bundle.getString(PTVConstants.TRANSPORT_TYPE), bundle.getInt(PTVConstants.STOP_ID),0,this);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void broadNextDeparturesResponse(final ArrayList<BroadNextDeparturesResult> broadNextDeparturesResults) {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                Fragment fragment = fragmentManager.findFragmentById(R.id.fragment);
                if(fragment!=null) {
                    ((IResults) fragment).setResults(broadNextDeparturesResults);
                }
            }
        });


    }

    public void nearMeResponse(ArrayList<NearMeResult> nearMeResults) {

    }

    public void disruptionsResponse(final ArrayList<Disruption>  disruptionsResults) {

    }

    public void stopsOnLineResponse(final ArrayList<Stop>  stopResults) {

    }


}
