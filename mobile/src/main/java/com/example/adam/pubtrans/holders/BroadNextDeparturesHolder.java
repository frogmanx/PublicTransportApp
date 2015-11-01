package com.example.adam.pubtrans.holders;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.activities.SecondaryActivity;
import com.example.adam.pubtrans.activities.TertiaryActivity;
import com.example.adam.pubtrans.interfaces.IFabAnimate;
import com.example.adam.pubtrans.models.BroadNextDeparturesResult;
import com.example.adam.pubtrans.models.NearMeResult;
import com.example.adam.pubtrans.utils.DateUtils;
import com.example.adam.pubtrans.utils.ISO8601;
import com.example.adam.pubtrans.utils.ImageUtils;
import com.example.adam.pubtrans.utils.PTVConstants;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Adam on 28/05/2015.
 */
public class BroadNextDeparturesHolder extends ValuesBaseHolder {

    private BroadNextDeparturesResult mBroadNextDeparturesResults;

    public BroadNextDeparturesHolder(View itemView) {
        super(itemView);
    }

    public void bindResult(BroadNextDeparturesResult broadNextDeparturesResult) {
        mBroadNextDeparturesResults = broadNextDeparturesResult;
        transportType.setText(mBroadNextDeparturesResults.platform.direction.directionName);
        locationName.setText(mBroadNextDeparturesResults.run.destinationName);

        timeTimeTableUTC.setText(DateUtils.convertToContext(mBroadNextDeparturesResults.timeTimeTableUTC, false));

        if(mBroadNextDeparturesResults.timeRealTimeUTC!=null) {
            realTimeTableUTC.setText(DateUtils.convertToContext(mBroadNextDeparturesResults.timeRealTimeUTC, false));

        }
        imageView.setImageResource(ImageUtils.getTransportImageResource(this.mBroadNextDeparturesResults.run.transportType));
    }

    @Override
    public void onClick(View v) {
        ((IFabAnimate)v.getContext()).shrinkFab();
        Intent intent = new Intent(v.getContext(), TertiaryActivity.class);
        intent.putExtra(PTVConstants.TRANSPORT_TYPE, mBroadNextDeparturesResults.platform.stop.transportType);
        intent.putExtra(PTVConstants.STOP_ID, mBroadNextDeparturesResults.platform.stop.stopId);
        intent.putExtra(PTVConstants.RUN_ID, mBroadNextDeparturesResults.run.runId);
        v.getContext().startActivity(intent);
    }


}
