package com.example.adam.pubtrans.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.adam.pubtrans.Crime;
import com.example.adam.pubtrans.CrimeHolder;
import com.example.adam.pubtrans.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 28/05/2015.
 */
public class MyAdapter extends RecyclerView.Adapter<CrimeHolder> {
    ArrayList<Crime> mCrimes;
    public MyAdapter(ArrayList<Crime> crimes){
        this.mCrimes = crimes;
    }

    @Override
    public CrimeHolder onCreateViewHolder(ViewGroup parent, int pos) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_crime, parent, false);
        return new CrimeHolder(view);
    }

    @Override
    public void onBindViewHolder(CrimeHolder holder, int pos) {
        Crime crime = mCrimes.get(pos);
        holder.bindCrime(crime);
    }

    @Override
    public int getItemCount() {
        return mCrimes.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}