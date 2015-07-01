package com.example.adam.pubtrans.fragments;

import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.activities.SecondaryActivity;
import com.example.adam.pubtrans.adapters.BroadNextDepaturesAdapter;
import com.example.adam.pubtrans.adapters.NearMeResultAdapter;
import com.example.adam.pubtrans.interfaces.IResults;
import com.example.adam.pubtrans.models.BroadNextDeparturesResult;
import com.example.adam.pubtrans.models.NearMeResult;
import com.example.adam.pubtrans.utils.ImageUtils;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

/**
 * Created by Adam on 31/05/2015.
 */
public class BroadNextDepaturesListFragment extends Fragment implements IResults<BroadNextDeparturesResult> {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<BroadNextDeparturesResult> results;
    private ArrayList<Marker> markerArrayList;

    TextView transportType;
    TextView locationName;
    ImageView imageView;
    RelativeLayout headerView;
    private NearMeResult mNearMeResult;





    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    public static final BroadNextDepaturesListFragment newInstance(String message) {
        BroadNextDepaturesListFragment f = new BroadNextDepaturesListFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);
        return f;
    }

    public BroadNextDepaturesListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.broad_next_depatures_fragment, container, false);
        RelativeLayout pageLoading = (RelativeLayout) v.findViewById(R.id.pageLoading);
        pageLoading.setVisibility(View.VISIBLE);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);

        transportType = (TextView) v.findViewById(R.id.transport_type);
        locationName = (TextView) v.findViewById(R.id.location_name);
        imageView = (ImageView)v.findViewById(R.id.image);
        headerView = (RelativeLayout) v.findViewById(R.id.header_item);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        results = new ArrayList<>();

        mAdapter = new BroadNextDepaturesAdapter(results);
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    public void onStart () {
        super.onStart();
        mNearMeResult = ((SecondaryActivity) getActivity()).getSelectedStop();



        transportType.setText(mNearMeResult.locationName);
        imageView.setImageResource(ImageUtils.getTransportImageResourceWhite(this.mNearMeResult.transportType));
    }

    public void refresh() {
        View v= getView();
        if(v!=null) {
            RelativeLayout pageLoading = (RelativeLayout) getView().findViewById(R.id.pageLoading);
            pageLoading.setVisibility(View.VISIBLE);
        }

    }

    public void animateHeader() {
        TransitionDrawable transition = (TransitionDrawable) headerView.getBackground();
        transition.startTransition(350);
    }

    public void reloadResults() {

    }

    public void setResults(ArrayList<BroadNextDeparturesResult> results) {

        View v = getView();
        if(v!=null) {
            this.results.clear();
            this.results.addAll(results);
            mAdapter.notifyDataSetChanged();

        }
        RelativeLayout pageLoading = (RelativeLayout) getView().findViewById(R.id.pageLoading);
        pageLoading.setVisibility(View.GONE);

    }
}
