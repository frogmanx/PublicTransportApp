package com.example.adam.pubtrans.holders;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.models.BroadNextDeparturesResult;
import com.example.adam.pubtrans.models.Disruption;
import com.example.adam.pubtrans.models.DisruptionsResult;
import com.example.adam.pubtrans.utils.ImageUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Adam on 28/05/2015.
 */
public class DisruptionsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @Bind(R.id.transport_type) TextView transportType;
    @Bind(R.id.location_name) TextView locationName;
    @Bind(R.id.time) TextView timeTimeTableUTC;
    @Bind(R.id.image) ImageView imageView;
    private Disruption mDisruptionsResult;

    public DisruptionsHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
    }

    public void bindResult(Disruption disruptionsResult) {
        mDisruptionsResult = disruptionsResult;
        transportType.setText(mDisruptionsResult.title);

        imageView.setImageResource(ImageUtils.getTransportImageResource(this.mDisruptionsResult.type));
    }

    @Override
    public void onClick(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mDisruptionsResult.url));
        v.getContext().startActivity(browserIntent);
    }


}
