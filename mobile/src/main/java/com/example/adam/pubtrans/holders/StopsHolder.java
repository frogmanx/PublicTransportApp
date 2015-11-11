package com.example.adam.pubtrans.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.models.BroadNextDeparturesResult;
import com.example.adam.pubtrans.models.Stop;
import com.example.adam.pubtrans.utils.ImageUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Adam on 28/05/2015.
 */
public class StopsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @Bind(R.id.transport_type) TextView transportType;
    @Bind(R.id.location_name) TextView locationName;
    @Bind(R.id.image) ImageView imageView;
    private Stop stop;

    public StopsHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
        imageView.setImageResource(ImageUtils.getTransportImageResource(this.stop.transportType));
    }

    public void bindResult(Stop stop) {
        this.stop = stop;
        transportType.setText(stop.locationName);
        locationName.setText(stop.suburb);
        imageView.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public void onClick(View v) {
    }


}