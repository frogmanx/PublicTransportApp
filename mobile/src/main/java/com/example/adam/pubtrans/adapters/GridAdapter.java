package com.example.adam.pubtrans.adapters;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.holders.BroadNextDeparturesHolder;
import com.example.adam.pubtrans.holders.DisruptionsHolder;
import com.example.adam.pubtrans.holders.NearMeResultHolder;
import com.example.adam.pubtrans.holders.StopsHolder;
import com.example.adam.pubtrans.models.BroadNextDeparturesResult;
import com.example.adam.pubtrans.models.Disruption;
import com.example.adam.pubtrans.models.NearMeResult;
import com.example.adam.pubtrans.models.Stop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 10/11/2015.
 */
public class GridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_DISRUPTION = 0;
    private static final int TYPE_BROAD_NEXT_DEPARTURES = 1;
    private static final int TYPE_NEAR_ME = 2;
    private static final int TYPE_STOP = 3;
    private static final int UNKNOWN = 4;
    private List<Object> items;

    public GridAdapter(Activity hostActivity, ArrayList list) {
        items = list;
        setHasStableIds(true);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_DISRUPTION:
                return new DisruptionsHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.holder_grid, parent, false));
            case TYPE_BROAD_NEXT_DEPARTURES:
                return new BroadNextDeparturesHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.holder_grid, parent, false));
            case TYPE_NEAR_ME:
                return new NearMeResultHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.holder_grid, parent, false));
            case TYPE_STOP:
                return new StopsHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.holder_grid, parent, false));
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < getDataItemCount()
                && getDataItemCount() > 0) {
            Object item = getItem(position);
            if (item instanceof Disruption) {
                ((DisruptionsHolder) holder).bindResult((Disruption) item);
            } else if (item instanceof BroadNextDeparturesResult) {
                ((BroadNextDeparturesHolder) holder).bindResult((BroadNextDeparturesResult) item);
            } else if (item instanceof NearMeResult) {
                ((NearMeResultHolder) holder).bindResult((NearMeResult) item, null);
            } else if (item instanceof Stop) {
                ((StopsHolder) holder).bindResult((Stop) item);
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position < getDataItemCount()
                && getDataItemCount() > 0) {
            Object item = getItem(position);
            if (item instanceof Disruption) {
                return TYPE_DISRUPTION;
            } else if (item instanceof BroadNextDeparturesResult) {
                return TYPE_BROAD_NEXT_DEPARTURES;
            } else if (item instanceof NearMeResult) {
                return TYPE_NEAR_ME;
            } else if (item instanceof Stop) {
                return TYPE_STOP;
            }
        }
        return UNKNOWN;
    }

    private Object getItem(int position) {
        return items.get(position);
    }

    public int getDataItemCount() {
        return items.size();
    }

    @Override
    public int getItemCount() {
        return getDataItemCount();
    }
}
