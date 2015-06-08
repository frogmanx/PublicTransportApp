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
import com.example.adam.pubtrans.adapters.DisruptionsAdapter;
import com.example.adam.pubtrans.interfaces.IResults;
import com.example.adam.pubtrans.models.Disruption;
import com.example.adam.pubtrans.models.NearMeResult;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Adam on 8/06/2015.
 */
public class MapViewFragment extends Fragment implements IResults<NearMeResult>, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<NearMeResult> results;

    private ArrayList<NearMeResult> filteredResults;
    private ArrayList<String> filter = new ArrayList<String>();

    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    public static final MapViewFragment newInstance(String message) {
        MapViewFragment f = new MapViewFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);
        return f;
    }

    public MapViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_fragment, container, false);

        results = ((MainActivity) getActivity()).getNearMeResults();
        filterResults();

        FloatingActionButton fab1 = (FloatingActionButton) v.findViewById(R.id.fab1);
        FloatingActionButton fab2 = (FloatingActionButton) v.findViewById(R.id.fab2);
        FloatingActionButton fab3 = (FloatingActionButton) v.findViewById(R.id.fab3);

        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);

        fab1.setSelected(true);
        fab2.setSelected(true);
        fab3.setSelected(true);

        return v;
    }

    public void refresh() {
        View v= getView();
        if(v!=null) {
            RelativeLayout pageLoading = (RelativeLayout) getView().findViewById(R.id.pageLoading);
            pageLoading.setVisibility(View.VISIBLE);
        }

    }

    public void setResults(ArrayList<NearMeResult> results) {
        View v = getView();
        if(v!=null) {
            this.results.clear();
            this.results.addAll(results);
            mAdapter.notifyDataSetChanged();
        }

    }

    public void onClick(View v) {
        if(v instanceof FloatingActionButton) {
            FloatingActionButton fab = (FloatingActionButton) v;
            fab.setSelected(!fab.isSelected());
            if(fab.isSelected()) {
                if(!filter.contains((String) fab.getTag())) {
                    filter.add((String)fab.getTag());
                }
            }
            else {
                if(filter.contains((String)fab.getTag())) {
                    filter.remove((String)fab.getTag());
                }
            }
            filterResults();
        }
    }
    public void reloadResults() {

    }

    public void filterResults() {
        if(filteredResults==null) {
            filteredResults = new ArrayList<>();
        }
        filteredResults.clear();
        for(Object result : results) {
            if(result instanceof NearMeResult) {
                NearMeResult nearMeResult = (NearMeResult) result;
                if (filter.contains(nearMeResult.transportType)) {
                    filteredResults.add(nearMeResult);
                }
            }
        }
        if(mAdapter!=null) {
            mAdapter.notifyDataSetChanged();
        }

    }
}
