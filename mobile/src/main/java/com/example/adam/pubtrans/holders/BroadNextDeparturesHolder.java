package com.example.adam.pubtrans.holders;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.activities.SecondaryActivity;
import com.example.adam.pubtrans.activities.TertiaryActivity;
import com.example.adam.pubtrans.models.BroadNextDeparturesResult;
import com.example.adam.pubtrans.models.NearMeResult;
import com.example.adam.pubtrans.utils.ISO8601;
import com.example.adam.pubtrans.utils.PTVConstants;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Adam on 28/05/2015.
 */
// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public class BroadNextDeparturesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView transportType;
    TextView locationName;
    TextView timeTimeTableUTC;
    ImageView imageView;
    private BroadNextDeparturesResult mBroadNextDeparturesResults;

    public BroadNextDeparturesHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        transportType = (TextView) itemView.findViewById(R.id.transport_type);
        locationName = (TextView) itemView.findViewById(R.id.location_name);
        timeTimeTableUTC = (TextView) itemView.findViewById(R.id.time);
        imageView = (ImageView)itemView.findViewById(R.id.image);
    }

    public void bindResult(BroadNextDeparturesResult broadNextDeparturesResult) {
        mBroadNextDeparturesResults = broadNextDeparturesResult;
        transportType.setText(mBroadNextDeparturesResults.platform.direction.directionName);
        locationName.setText(mBroadNextDeparturesResults.run.destinationName);

        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz", Locale.US);
        Date now = new Date();
        try{
            Calendar scheduled = ISO8601.toCalendar(mBroadNextDeparturesResults.timeTimeTableUTC);
            sdf.setTimeZone(scheduled.getTimeZone());
            sdf.format(scheduled.getTime());
            long difference = scheduled.getTime().getTime()-now.getTime();
            timeTimeTableUTC.setText(sdf.format(scheduled.getTime()) + " (" + ISO8601.convertToTimeContext(difference) + ")");
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        imageView.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), TertiaryActivity.class);
        intent.putExtra(PTVConstants.TRANSPORT_TYPE, mBroadNextDeparturesResults.platform.stop.transportType);
        intent.putExtra(PTVConstants.LINE_ID, mBroadNextDeparturesResults.platform.direction.line.lineId);
        v.getContext().startActivity(intent);
    }


}
