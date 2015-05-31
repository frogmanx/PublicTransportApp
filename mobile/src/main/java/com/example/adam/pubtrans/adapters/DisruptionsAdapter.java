package com.example.adam.pubtrans.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.holders.BroadNextDeparturesHolder;
import com.example.adam.pubtrans.holders.DisruptionsHolder;
import com.example.adam.pubtrans.models.BroadNextDeparturesResult;
import com.example.adam.pubtrans.models.Disruption;

import java.util.ArrayList;

/**
 * Created by Adam on 31/05/2015.
 */
public class DisruptionsAdapter extends RecyclerView.Adapter<DisruptionsHolder> {
    ArrayList<Disruption> mDisruptionResults;
    public DisruptionsAdapter(ArrayList<Disruption> disruptionResults){
        this.mDisruptionResults = disruptionResults;
    }

    @Override
    public DisruptionsHolder onCreateViewHolder(ViewGroup parent, int pos) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_result, parent, false);
        return new DisruptionsHolder(view);
    }

    @Override
    public void onBindViewHolder(DisruptionsHolder holder, int pos) {
        Disruption nearMeResult = mDisruptionResults.get(pos);
        holder.bindResult(nearMeResult);
    }

    @Override
    public int getItemCount() {
        return mDisruptionResults.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}