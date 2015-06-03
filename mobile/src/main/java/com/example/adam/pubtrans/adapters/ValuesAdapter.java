package com.example.adam.pubtrans.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.holders.BroadNextDeparturesHolder;
import com.example.adam.pubtrans.holders.ValuesHolder;
import com.example.adam.pubtrans.models.BroadNextDeparturesResult;
import com.example.adam.pubtrans.models.Values;

import java.util.ArrayList;

/**
 * Created by Adam on 31/05/2015.
 */
public class ValuesAdapter extends RecyclerView.Adapter<ValuesHolder> {
    ArrayList<Values> mValuesResults;
    public ValuesAdapter(ArrayList<Values> valuesResults){
        this.mValuesResults = valuesResults;
    }

    @Override
    public ValuesHolder onCreateViewHolder(ViewGroup parent, int pos) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_result, parent, false);
        return new ValuesHolder(view);
    }

    @Override
    public void onBindViewHolder(ValuesHolder holder, int pos) {
        Values nearMeResult = mValuesResults.get(pos);
        holder.bindResult(nearMeResult);
    }

    @Override
    public int getItemCount() {
        return mValuesResults.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}