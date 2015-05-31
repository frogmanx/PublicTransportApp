package com.example.adam.pubtrans.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.holders.DisruptionsHolder;
import com.example.adam.pubtrans.holders.StopsHolder;
import com.example.adam.pubtrans.models.Disruption;
import com.example.adam.pubtrans.models.Stop;

import java.util.ArrayList;

/**
 * Created by Adam on 31/05/2015.
 */
public class StopsResultAdapter extends RecyclerView.Adapter<StopsHolder> {
    ArrayList<Stop> mStopResults;
    public StopsResultAdapter(ArrayList<Stop> stopResults){
        this.mStopResults = stopResults;
    }

    @Override
    public StopsHolder onCreateViewHolder(ViewGroup parent, int pos) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_result, parent, false);
        return new StopsHolder(view);
    }

    @Override
    public void onBindViewHolder(StopsHolder holder, int pos) {
        Stop stopResult = mStopResults.get(pos);
        holder.bindResult(stopResult);
    }

    @Override
    public int getItemCount() {
        return mStopResults.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}