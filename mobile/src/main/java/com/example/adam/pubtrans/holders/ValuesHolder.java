package com.example.adam.pubtrans.holders;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.interfaces.IAddTimer;
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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Adam on 28/05/2015.
 */
public class ValuesHolder extends ValuesBaseHolder {

    private Values values;

    public ValuesHolder(View itemView) {
        super(itemView);
    }

    public void bindResult(Values values) {
        this.values = values;
        transportType.setText(this.values.platform.stop.locationName);
        locationName.setText(this.values.run.destinationName);
        imageView.setImageResource(ImageUtils.getTransportImageResource(this.values.run.transportType));
        Log.e("ValuesHolder" , this.values.timeTable);


        if(this.values.realTime!=null) {
            timeTimeTableUTC.setText(DateUtils.convertToContext(this.values.realTime, false));
            realTimeTableUTC.setText(DateUtils.convertToContext(this.values.timeTable, this.values.realTime, false));
        }
        else {
            timeTimeTableUTC.setText(DateUtils.convertToContext(this.values.timeTable, false));
        }
    }

    public void onClick(View v) {
        ((IAddTimer)v.getContext()).showAddTimerView(v, values);
    }
}