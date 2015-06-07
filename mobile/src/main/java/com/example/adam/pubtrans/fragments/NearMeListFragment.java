package com.example.adam.pubtrans.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.activities.MainActivity;
import com.example.adam.pubtrans.adapters.NearMeResultAdapter;
import com.example.adam.pubtrans.interfaces.IResults;
import com.example.adam.pubtrans.models.NearMeResult;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Adam on 31/05/2015.
 */
public class NearMeListFragment extends Fragment implements IResults<NearMeResult>, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<NearMeResult> results;
    private ArrayList<NearMeResult> filteredResults;
    private ArrayList<String> filter = new ArrayList<String>();

    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    public static final NearMeListFragment newInstance(String message) {
        NearMeListFragment f = new NearMeListFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);
        return f;
    }

    public NearMeListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        filter.add("train");
        filter.add("tram");
        filter.add("bus");
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        results = ((MainActivity)getActivity()).getNearMeResults();

        filterResults();

        mAdapter = new NearMeResultAdapter(filteredResults);
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab1 = (FloatingActionButton) v.findViewById(R.id.fab1);
        FloatingActionButton fab2 = (FloatingActionButton) v.findViewById(R.id.fab2);
        FloatingActionButton fab3 = (FloatingActionButton) v.findViewById(R.id.fab3);

        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_scale_inverse);

        FloatingActionMenu fabmenu = (FloatingActionMenu) v.findViewById(R.id.menu1);
        fabmenu.setMenuButtonShowAnimation(animation);

        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);

        fab1.setSelected(true);
        fab2.setSelected(true);
        fab3.setSelected(true);




        return v;
    }

    public void refresh() {

    }

    public void setResults(ArrayList<NearMeResult> results) {
        if(this.results==null) {
            this.results = new ArrayList<>();
        }
        this.results.clear();
        this.results.addAll(results);
        filterResults();

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

    public void filterResults() {
        if(filteredResults==null) {
            filteredResults = new ArrayList<>();
        }
        filteredResults.clear();
        for(NearMeResult result : results) {
            if(filter.contains(result.transportType)){
                filteredResults.add(result);
            }
        }
        if(mAdapter!=null) {
            mAdapter.notifyDataSetChanged();
        }

    }

}
