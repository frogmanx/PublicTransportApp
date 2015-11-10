package com.example.adam.pubtrans.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.adapters.DisruptionsAdapter;
import com.example.adam.pubtrans.fragments.DisruptionsFragment;
import com.example.adam.pubtrans.interfaces.Callback;
import com.example.adam.pubtrans.interfaces.IPubActivity;
import com.example.adam.pubtrans.models.Disruption;
import com.example.adam.pubtrans.models.DisruptionsResult;
import com.example.adam.pubtrans.models.NearMeResult;
import com.example.adam.pubtrans.models.Values;
import com.example.adam.pubtrans.utils.PTVConstants;
import com.example.adam.pubtrans.utils.WebApi;
import com.example.adam.pubtrans.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Adam on 23/06/2015.
 */
public class DisruptionsActivity extends BaseActivity implements IPubActivity, Callback<DisruptionsResult> {

    @Bind(R.id.anim_toolbar) Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
    ArrayList<Fragment> fragments;

    ArrayList<Disruption>  disruptionsResults;

    @Bind(R.id.scrollableview) RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disruptions);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        collapsingToolbar.setTitle("Disruptions");

        setTitle("Disruptions");
        disruptionsResults = new ArrayList<>();
        fragments = (ArrayList<Fragment>) getFragments();



        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        disruptionsResults = getDisruptionsResults();

        mAdapter = new DisruptionsAdapter(disruptionsResults);
        mRecyclerView.setAdapter(mAdapter);

        try {
            WebApi.getDisruptions(this);

        }catch (Exception e) {
            e.printStackTrace();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    private List<Fragment> getFragments(){

        List<Fragment> fList = new ArrayList<>();
        fList.add(DisruptionsFragment.newInstance("Fragment 1"));

        return fList;

    }


    public ArrayList<Disruption> getDisruptionsResults() {
        return disruptionsResults;
    }

    public ArrayList<NearMeResult> getNearMeResults() {
        return null;
    }
    public ArrayList<Values> getAlarms() {
        return null;
    }

    public void setTypeInArrayList(ArrayList<Disruption> arrayList, String type) {
        for(int i = 0; i < arrayList.size(); i++) {
            arrayList.get(i).type = type;
        }
    }

    @Override
    public void success(final DisruptionsResult mydisruptionsResults) {
        setTypeInArrayList(mydisruptionsResults.general, "general");
        setTypeInArrayList(mydisruptionsResults.metroBus, PTVConstants.BUS_TYPE);
        setTypeInArrayList(mydisruptionsResults.metroTrain, PTVConstants.TRAIN_TYPE);
        setTypeInArrayList(mydisruptionsResults.metroTram, PTVConstants.TRAM_TYPE);
        setTypeInArrayList(mydisruptionsResults.regionalBus, PTVConstants.BUS_TYPE);
        setTypeInArrayList(mydisruptionsResults.regionalCoach, PTVConstants.BUS_TYPE);
        setTypeInArrayList(mydisruptionsResults.regionalTrain, PTVConstants.TRAIN_TYPE);
        runOnUiThread(new Runnable() {

            public void run() {
                disruptionsResults.clear();
                disruptionsResults.addAll(mydisruptionsResults.general);
                disruptionsResults.addAll(mydisruptionsResults.metroBus);
                disruptionsResults.addAll(mydisruptionsResults.metroTrain);
                disruptionsResults.addAll(mydisruptionsResults.metroTram);
                disruptionsResults.addAll(mydisruptionsResults.regionalBus);
                disruptionsResults.addAll(mydisruptionsResults.regionalCoach);
                disruptionsResults.addAll(mydisruptionsResults.regionalTrain);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            // app icon in action bar clicked; goto parent activity.
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
