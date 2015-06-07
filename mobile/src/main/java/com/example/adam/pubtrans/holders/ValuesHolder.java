package com.example.adam.pubtrans.holders;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.models.BroadNextDeparturesResult;
import com.example.adam.pubtrans.models.Stop;
import com.example.adam.pubtrans.models.Values;
import com.example.adam.pubtrans.utils.DateUtils;
import com.example.adam.pubtrans.utils.ISO8601;
import com.example.adam.pubtrans.utils.ImageUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Adam on 28/05/2015.
 */
// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public class ValuesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView transportType;
    TextView locationName;
    TextView timeTimeTableUTC;

    TextView realTimeTableUTC;
    ImageView imageView;
    private Values values;

    public ValuesHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        transportType = (TextView) itemView.findViewById(R.id.transport_type);
        locationName = (TextView) itemView.findViewById(R.id.location_name);
        timeTimeTableUTC = (TextView) itemView.findViewById(R.id.time);

        realTimeTableUTC = (TextView) itemView.findViewById(R.id.time2);
        imageView = (ImageView)itemView.findViewById(R.id.image);
    }

    public void bindResult(Values values) {
        this.values = values;
        transportType.setText(this.values.platform.stop.locationName);
        locationName.setText(this.values.run.destinationName);
        imageView.setImageResource(ImageUtils.getTransportImageResource(this.values.run.transportType));
        Log.e("ValuesHolder" , this.values.timeTable);


        if(values.timeTable!=null) {
            timeTimeTableUTC.setText(DateUtils.convertToContext(this.values.realTime, false));
            realTimeTableUTC.setText(DateUtils.convertToContext(this.values.timeTable, this.values.realTime, false));
        }
    }

    @Override
    public void onClick(View v) {
    }


}
