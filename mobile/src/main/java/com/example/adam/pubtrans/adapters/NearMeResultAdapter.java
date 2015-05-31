package com.example.adam.pubtrans.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adam.pubtrans.holders.NearMeResultHolder;
import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.models.NearMeResult;

import java.util.ArrayList;

/**
 * Created by Adam on 31/05/2015.
 */
public class NearMeResultAdapter extends RecyclerView.Adapter<NearMeResultHolder> {
    ArrayList<NearMeResult> mNearMeResults;
    public NearMeResultAdapter(ArrayList<NearMeResult> nearMeResults){
        this.mNearMeResults = nearMeResults;
    }

    @Override
    public NearMeResultHolder onCreateViewHolder(ViewGroup parent, int pos) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_result, parent, false);
        return new NearMeResultHolder(view);
    }

    @Override
    public void onBindViewHolder(NearMeResultHolder holder, int pos) {
        NearMeResult nearMeResult = mNearMeResults.get(pos);
        holder.bindResult(nearMeResult);
    }

    @Override
    public int getItemCount() {
        return mNearMeResults.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
