package com.example.adam.pubtrans;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Adam on 28/05/2015.
 */
// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView personName;
    TextView personAge;
    ImageView personPhoto;
    CheckBox isAvailable;
    private Crime mCrime;

    public CrimeHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        personName = (TextView)itemView.findViewById(R.id.person_name);
        personAge = (TextView) itemView.findViewById(R.id.person_age);
        personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        isAvailable = (CheckBox)itemView.findViewById(R.id.is_available);
    }

    public void bindCrime(Crime crime) {
        mCrime = crime;
        personName.setText(crime.name);
        personAge.setText(crime.age);
        personPhoto.setImageResource(crime.photoId);
    }

    @Override
    public void onClick(View v) {
        if (mCrime != null) {
            isAvailable.setChecked(true);
        }
    }
}
