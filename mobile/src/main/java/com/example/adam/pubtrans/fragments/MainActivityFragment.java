package com.example.adam.pubtrans.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adam.pubtrans.Crime;
import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.adapters.MyAdapter;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Crime> persons;
    private ArrayList<Marker> markerArrayList;

    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    public static final MainActivityFragment newInstance(String message) {
        MainActivityFragment f = new MainActivityFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);
        return f;
    }

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        persons = new ArrayList<>();
        persons.add(new Crime("Emma Wilson", "23 years old", R.mipmap.ic_launcher));
        persons.add(new Crime("Lavery Maiss", "25 years old", R.mipmap.ic_launcher));
        persons.add(new Crime("Lillie Watts", "35 years old", R.mipmap.ic_launcher));

        mAdapter = new MyAdapter(persons);
        mRecyclerView.setAdapter(mAdapter);


        return v;
    }
}
