package com.example.adam.pubtrans.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.activities.MainActivity;
import com.example.adam.pubtrans.activities.TertiaryActivity;
import com.example.adam.pubtrans.adapters.StopsResultAdapter;
import com.example.adam.pubtrans.interfaces.IResults;
import com.example.adam.pubtrans.models.Stop;
import com.example.adam.pubtrans.views.DividerItemDecoration;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Adam on 31/05/2015.
 */
public class StopsListFragment extends Fragment implements IResults<Stop> {

    @Bind(R.id.my_recycler_view) RecyclerView recyclerView;
    @Bind(R.id.pageLoading) RelativeLayout pageLoading;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Stop> mResults;

    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    public static final StopsListFragment newInstance(String message) {
        StopsListFragment f = new StopsListFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);
        return f;
    }

    public StopsListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.stop_list_fragment, container, false);
        ButterKnife.bind(this, v);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mResults = ((TertiaryActivity)getActivity()).getStopsList();

        mAdapter = new StopsResultAdapter(mResults);
        recyclerView.setAdapter(mAdapter);


        return v;
    }

    public void refresh() {
        if(pageLoading!=null) {
            pageLoading.setVisibility(View.VISIBLE);
        }

    }
    public void reloadResults() {

    }

    public void setResults(ArrayList<Stop> results) {
        this.mResults.clear();
        this.mResults.addAll(results);
        mAdapter.notifyDataSetChanged();
        pageLoading.setVisibility(View.GONE);
    }
}
