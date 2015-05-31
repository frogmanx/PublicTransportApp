package com.example.adam.pubtrans.holders;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adam.pubtrans.utils.PTVConstants;
import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.activities.SecondaryActivity;
import com.example.adam.pubtrans.models.NearMeResult;

/**
 * Created by Adam on 28/05/2015.
 */
// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public class NearMeResultHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView transportType;
    TextView locationName;
    ImageView imageView;
    private NearMeResult mNearMeResult;

    public NearMeResultHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        transportType = (TextView) itemView.findViewById(R.id.transport_type);
        locationName = (TextView) itemView.findViewById(R.id.location_name);
        imageView = (ImageView)itemView.findViewById(R.id.image);
    }

    public void bindResult(NearMeResult nearMeResult) {
        mNearMeResult = nearMeResult;
        transportType.setText(mNearMeResult.transportType);
        locationName.setText(mNearMeResult.locationName);
        imageView.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(v.getContext(), SecondaryActivity.class);
        intent.putExtra(PTVConstants.TRANSPORT_TYPE, mNearMeResult.transportType);
        intent.putExtra(PTVConstants.STOP_ID, mNearMeResult.stopId);
        v.getContext().startActivity(intent);

    }


}
