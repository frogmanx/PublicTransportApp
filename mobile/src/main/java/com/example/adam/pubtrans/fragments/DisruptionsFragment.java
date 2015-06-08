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
import com.example.adam.pubtrans.adapters.BroadNextDepaturesAdapter;
import com.example.adam.pubtrans.adapters.DisruptionsAdapter;
import com.example.adam.pubtrans.interfaces.IResults;
import com.example.adam.pubtrans.models.BroadNextDeparturesResult;
import com.example.adam.pubtrans.models.Disruption;
import com.example.adam.pubtrans.models.DisruptionsResult;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

/**
 * Created by Adam on 31/05/2015.
 */
public class DisruptionsFragment extends Fragment implements IResults<Disruption> {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Disruption> results;

    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    public static final DisruptionsFragment newInstance(String message) {
        DisruptionsFragment f = new DisruptionsFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);
        return f;
    }

    public DisruptionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        results = ((MainActivity) getActivity()).getDisruptionsResults();

        mAdapter = new DisruptionsAdapter(results);
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

    public void reloadResults() {

    }

    public void setResults(ArrayList<Disruption> results) {
        View v = getView();
        if(v!=null) {
            this.results.clear();
            this.results.addAll(results);
            mAdapter.notifyDataSetChanged();
        }

    }
}