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

/**
 * Created by Adam on 28/05/2015.
 */
// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public class DisruptionsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView transportType;
    TextView locationName;
    TextView timeTimeTableUTC;
    ImageView imageView;
    private Disruption mDisruptionsResult;

    public DisruptionsHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        transportType = (TextView) itemView.findViewById(R.id.transport_type);
        locationName = (TextView) itemView.findViewById(R.id.location_name);
        timeTimeTableUTC = (TextView) itemView.findViewById(R.id.time);
        imageView = (ImageView)itemView.findViewById(R.id.image);
    }

    public void bindResult(Disruption disruptionsResult) {
        mDisruptionsResult = disruptionsResult;
        transportType.setText(mDisruptionsResult.title);
        locationName.setText(mDisruptionsResult.type);
        timeTimeTableUTC.setText(mDisruptionsResult.description);


        imageView.setImageResource(ImageUtils.getTransportImageResource(this.mDisruptionsResult.type));
    }

    @Override
    public void onClick(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mDisruptionsResult.url));
        v.getContext().startActivity(browserIntent);
    }


}
