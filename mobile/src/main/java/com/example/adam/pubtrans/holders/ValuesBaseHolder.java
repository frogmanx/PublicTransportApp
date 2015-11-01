package com.example.adam.pubtrans.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.models.Values;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Adam on 1/11/2015.
 */
public abstract class ValuesBaseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @Bind(R.id.transport_type)
    TextView transportType;
    @Bind(R.id.location_name) TextView locationName;
    @Bind(R.id.time) TextView timeTimeTableUTC;
    @Bind(R.id.time2) TextView realTimeTableUTC;
    @Bind(R.id.image)
    ImageView imageView;
    private Values values;

    public ValuesBaseHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
    }

}
