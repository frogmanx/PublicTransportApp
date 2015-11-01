package com.example.adam.pubtrans.holders;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.models.Disruption;
import com.example.adam.pubtrans.utils.ImageUtils;

/**
 * Created by Adam on 28/06/2015.
 */
public class HeaderHolder extends RecyclerView.ViewHolder {
    TextView titleView;

    public HeaderHolder(View itemView) {
        super(itemView);

        titleView = (TextView) itemView.findViewById(R.id.title);
    }

    public void bindResult(String title) {
        titleView.setText(title);
    }

}
