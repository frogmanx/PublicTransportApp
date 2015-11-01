package com.example.adam.pubtrans.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adam.pubtrans.holders.HeaderHolder;
import com.example.adam.pubtrans.holders.NearMeResultHolder;
import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.models.NearMeResult;
import com.example.adam.pubtrans.utils.PTVConstants;

import java.util.ArrayList;

/**
 * Created by Adam on 31/05/2015.
 */
public class NearMeResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<NearMeResult> mNearMeResults;
    int subPosition;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public NearMeResultAdapter(ArrayList<NearMeResult> nearMeResults){
        this.mNearMeResults = nearMeResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_result, parent, false);
            return new NearMeResultHolder(view);
        }
        else if(viewType==TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_item_2, parent, false);
            return new HeaderHolder(view);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");

    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return mNearMeResults.get(position).type== PTVConstants.HEADER_TYPE;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int pos) {
        if(holder instanceof NearMeResultHolder) {
            NearMeResult nearMeResult = mNearMeResults.get(pos);
            ((NearMeResultHolder) holder).bindResult(nearMeResult);
        }
        else if(holder instanceof HeaderHolder) {
            ((HeaderHolder) holder).bindResult(mNearMeResults.get(pos).result.locationName);
        }

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
