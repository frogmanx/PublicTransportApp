package com.example.adam.pubtrans.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adam.pubtrans.holders.BroadNextDeparturesHolder;
import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.models.BroadNextDeparturesResult;

import java.util.ArrayList;

/**
 * Created by Adam on 31/05/2015.
 */
public class BroadNextDepaturesAdapter extends RecyclerView.Adapter<BroadNextDeparturesHolder> {
    ArrayList<BroadNextDeparturesResult> mBroadNextDepatureResults;
    public BroadNextDepaturesAdapter(ArrayList<BroadNextDeparturesResult> nearMeResults){
        this.mBroadNextDepatureResults = nearMeResults;
    }

    @Override
    public BroadNextDeparturesHolder onCreateViewHolder(ViewGroup parent, int pos) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_result, parent, false);
        return new BroadNextDeparturesHolder(view);
    }

    @Override
    public void onBindViewHolder(BroadNextDeparturesHolder holder, int pos) {
        BroadNextDeparturesResult nearMeResult = mBroadNextDepatureResults.get(pos);
        holder.bindResult(nearMeResult);
    }

    @Override
    public int getItemCount() {
        return mBroadNextDepatureResults.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}