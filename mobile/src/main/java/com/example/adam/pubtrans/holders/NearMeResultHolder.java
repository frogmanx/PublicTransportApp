package com.example.adam.pubtrans.holders;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adam.pubtrans.interfaces.IFabAnimate;
import com.example.adam.pubtrans.utils.ImageUtils;
import com.example.adam.pubtrans.utils.PTVConstants;
import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.activities.SecondaryActivity;
import com.example.adam.pubtrans.models.NearMeResult;
import com.google.gson.Gson;

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
        transportType.setText(mNearMeResult.locationName);


        imageView.setImageResource(ImageUtils.getTransportImageResource(this.mNearMeResult.transportType));
    }

    @Override
    public void onClick(View v) {
        ((IFabAnimate)v.getContext()).shrinkFab();
        Intent intent = new Intent(v.getContext(), SecondaryActivity.class);
        Gson gson = new Gson();
        Log.e("NearMeResultHolder", mNearMeResult.locationName);
        String jsonNearMeResult = gson.toJson(mNearMeResult);
        intent.putExtra(PTVConstants.JSON_NEARMERESULT, jsonNearMeResult);

        //Get location on screen for tapped view
        int[] startingLocation = new int[2];
        v.getLocationOnScreen(startingLocation);
        intent.putExtra(SecondaryActivity.ARG_DRAWING_START_LOCATION, startingLocation[1]);

        v.getContext().startActivity(intent);
        ((AppCompatActivity) v.getContext()).overridePendingTransition(0, 0);

    }


}
