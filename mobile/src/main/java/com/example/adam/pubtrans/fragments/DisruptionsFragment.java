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
import com.example.adam.pubtrans.interfaces.IPubActivity;
import com.example.adam.pubtrans.interfaces.IResults;
import com.example.adam.pubtrans.models.BroadNextDeparturesResult;
import com.example.adam.pubtrans.models.Disruption;
import com.example.adam.pubtrans.models.DisruptionsResult;
import com.example.adam.pubtrans.views.DividerItemDecoration;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Adam on 31/05/2015.
 */
public class DisruptionsFragment extends Fragment implements IResults<Disruption> {

    @Bind(R.id.my_recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.pageLoading) RelativeLayout pageLoading;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Disruption> mResults;

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
        ButterKnife.bind(this, v);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mResults = ((IPubActivity) getActivity()).getDisruptionsResults();

        mAdapter = new DisruptionsAdapter(mResults);
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    public void refresh() {
        if(pageLoading!=null) {
            pageLoading.setVisibility(View.VISIBLE);
        }

    }

    public void reloadResults() {

    }

    public void setResults(ArrayList<Disruption> results) {
        View v = getView();
        if(v!=null) {
            this.mResults.clear();
            this.mResults.addAll(results);
            mAdapter.notifyDataSetChanged();
        }

    }
}