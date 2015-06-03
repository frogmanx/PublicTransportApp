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
import com.example.adam.pubtrans.activities.TertiaryActivity;
import com.example.adam.pubtrans.adapters.ValuesAdapter;
import com.example.adam.pubtrans.interfaces.IResults;
import com.example.adam.pubtrans.models.Values;

import java.util.ArrayList;

/**
 * Created by Adam on 4/06/2015.
 */
public class ValuesListFragment extends Fragment implements IResults<Values> {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Values> results;

    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    public static final ValuesListFragment newInstance(String message) {
        ValuesListFragment f = new ValuesListFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);
        return f;
    }

    public ValuesListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.broad_next_depatures_fragment, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        results = ((TertiaryActivity)getActivity()).getValuesList();

        mAdapter = new ValuesAdapter(results);
        mRecyclerView.setAdapter(mAdapter);


        return v;
    }

    public void refresh() {
        View v= getView();
        if(v!=null) {
            RelativeLayout pageLoading = (RelativeLayout) getView().findViewById(R.id.pageLoading);
            pageLoading.setVisibility(View.VISIBLE);
        }

    }

    public void setResults(ArrayList<Values> results) {
        this.results.clear();
        this.results.addAll(results);
        mAdapter.notifyDataSetChanged();
        RelativeLayout pageLoading = (RelativeLayout) getView().findViewById(R.id.pageLoading);
        pageLoading.setVisibility(View.GONE);
    }
}

