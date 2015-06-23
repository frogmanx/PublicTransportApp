package com.example.adam.pubtrans.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.androidmapsextensions.SupportMapFragment;
import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.adapters.DisruptionsAdapter;
import com.example.adam.pubtrans.adapters.MyFragmentPagerAdapter;
import com.example.adam.pubtrans.fragments.DisruptionsFragment;
import com.example.adam.pubtrans.fragments.NearMeListFragment;
import com.example.adam.pubtrans.fragments.TramSimulatorFragment;
import com.example.adam.pubtrans.interfaces.IPubActivity;
import com.example.adam.pubtrans.interfaces.IResults;
import com.example.adam.pubtrans.models.Disruption;
import com.example.adam.pubtrans.models.NearMeResult;
import com.example.adam.pubtrans.utils.WebApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 23/06/2015.
 */
public class DisruptionsActivity extends BaseActivity implements IPubActivity {

    ViewPager mViewPager;
    ArrayList<Fragment> fragments;

    ArrayList<Disruption>  disruptionsResults;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disruptions);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        setTitle("Disruptions");
        disruptionsResults = new ArrayList<>();
        mViewPager = (ViewPager) findViewById(R.id.pager);
        fragments = (ArrayList<Fragment>) getFragments();

        MyFragmentPagerAdapter fragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
       // mViewPager.setAdapter(fragmentPagerAdapter);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        disruptionsResults = getDisruptionsResults();

        mAdapter = new DisruptionsAdapter(disruptionsResults);
        mRecyclerView.setAdapter(mAdapter);

        try {
            WebApi.getDisruptions(this);

        }catch (Exception e) {
            e.printStackTrace();
        }

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

    @Override
    public void disruptionsResponse(final ArrayList<Disruption>  mydisruptionsResults) {
        runOnUiThread(new Runnable()
        {

            public void run()
            {
                disruptionsResults.clear();
                disruptionsResults.addAll(mydisruptionsResults);
                mAdapter.notifyDataSetChanged();
            }
        });
    }


}
