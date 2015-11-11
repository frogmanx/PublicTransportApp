package com.example.adam.pubtrans.holders;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adam.pubtrans.interfaces.IFabAnimate;
import com.example.adam.pubtrans.utils.ImageUtils;
import com.example.adam.pubtrans.utils.LocationUtil;
import com.example.adam.pubtrans.utils.PTVConstants;
import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.activities.SecondaryActivity;
import com.example.adam.pubtrans.models.NearMeResult;
import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Adam on 28/05/2015.
 */
public class NearMeResultHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @Bind(R.id.location_name) TextView transportType;
    @Bind(R.id.transport_type)  TextView locationName;
    @Bind(R.id.image) ImageView imageView;
    private NearMeResult mNearMeResult;
    private Location mLocation;

    public NearMeResultHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
    }

    public void bindResult(NearMeResult nearMeResult, Location location) {
        mNearMeResult = nearMeResult;
        mLocation = location;
        if(mNearMeResult.result!=null) {

            transportType.setText(mNearMeResult.result.locationName);
            imageView.setImageResource(ImageUtils.getTransportImageResource(this.mNearMeResult.result.transportType));
            if(mLocation!=null) {
                double distance = LocationUtil.distance(mLocation.getLatitude(), mLocation.getLongitude(), mNearMeResult.result.latitude,  mNearMeResult.result.longitude, 'K');
                String distanceAwayString = String.format("%.1f", distance) + "km away";
                locationName.setText(mNearMeResult.result.suburb + " (" + distanceAwayString + ")");
            }
            else {
                locationName.setText(mNearMeResult.result.suburb);
            }
        }


    }

    @Override
    public void onClick(View v) {
        if(v.getContext() instanceof IFabAnimate) {
            ((IFabAnimate)v.getContext()).shrinkFab();
        }

        Intent intent = new Intent(v.getContext(), SecondaryActivity.class);
        intent.putExtra(PTVConstants.JSON_NEARMERESULT, mNearMeResult);

        //Get location on screen for tapped view
        int[] startingLocation = new int[2];
        v.getLocationOnScreen(startingLocation);
        intent.putExtra(SecondaryActivity.ARG_DRAWING_START_LOCATION, startingLocation[1]);

        v.getContext().startActivity(intent);
        ((AppCompatActivity) v.getContext()).overridePendingTransition(0, 0);

    }


}
