package com.example.adam.pubtrans.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TextView;

import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.adapters.ValuesAdapter;
import com.example.adam.pubtrans.fragments.DisruptionsFragment;
import com.example.adam.pubtrans.interfaces.IAddTimer;
import com.example.adam.pubtrans.interfaces.IPubActivity;
import com.example.adam.pubtrans.models.Disruption;
import com.example.adam.pubtrans.models.NearMeResult;
import com.example.adam.pubtrans.models.Values;
import com.example.adam.pubtrans.receivers.AlarmReceiver;
import com.example.adam.pubtrans.utils.DateUtils;
import com.example.adam.pubtrans.utils.PTVConstants;
import com.example.adam.pubtrans.utils.SharedPreferencesHelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Adam on 4/07/2015.
 */
public class AlarmsActivity extends BaseActivity implements IPubActivity,IAddTimer {

    ArrayList<Fragment> mFragments;
    ArrayList<Values>  mAlarms;
    private Values mAlarmValues;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Bind(R.id.scrollableview) RecyclerView recylerView;
    @Bind(R.id.timer_card_view) CardView timerCardView;
    @Bind(R.id.anim_toolbar) Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disruptions);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        cleanseAlarms();

        collapsingToolbar.setTitle("Alarms");

        setTitle("Alarms");
        mAlarms = new ArrayList<>();
        mFragments = (ArrayList<Fragment>) getFragments();

        mLayoutManager = new LinearLayoutManager(this);
        recylerView.setLayoutManager(mLayoutManager);

        mAdapter = new ValuesAdapter(mAlarms);
        recylerView.setAdapter(mAdapter);

        updateAlarms();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private List<Fragment> getFragments(){

        List<Fragment> fList = new ArrayList<>();
        fList.add(DisruptionsFragment.newInstance("Fragment 1"));

        return fList;

    }

    public ArrayList<Disruption> getDisruptionsResults() {
        return null;
    }
    public ArrayList<NearMeResult> getNearMeResults() {
        return null;
    }
    public ArrayList<Values> getAlarms() {return mAlarms;}

    public void showAddTimerView(View view, Values values) {

        Gson gson = new Gson();
        String jsonValues = gson.toJson(values);
        if(SharedPreferencesHelper.isAlarmActivated(this, jsonValues)) {
            ((TextView) ButterKnife.findById(timerCardView,R.id.card_text)).setText("Remove Alarm");
        }
        else {
            ((TextView) ButterKnife.findById(timerCardView, R.id.card_text)).setText("Activate Alarm");
        }

        mAlarmValues = values;
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;
        float radius = Math.max(timerCardView.getWidth(), timerCardView.getHeight()) * 2.0f;

        if (timerCardView.getVisibility() == View.INVISIBLE) {
            timerCardView.setVisibility(View.VISIBLE);
            ViewAnimationUtils.createCircularReveal(timerCardView, cx, cy, 0, radius).start();
        } else {
            Animator reveal = ViewAnimationUtils.createCircularReveal(
                    timerCardView, cx, cy, radius, 0);
            reveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    //myView.setVisibility(View.INVISIBLE);
                }
            });
            reveal.start();
        }
    }

    public void cleanseAlarms() {

        ArrayList<String> tempArray = SharedPreferencesHelper.getAlarms(this);
        Gson gson = new Gson();
        for(int i = 0; i < tempArray.size();i++) {
            String stringValue = tempArray.get(i);
            Values value = gson.fromJson(tempArray.get(i), Values.class);
            double x;
            if(!value.realTime.contentEquals("null")) {
                x = DateUtils.convertToMSAway(value.realTime);
            }
            else {
                x = DateUtils.convertToMSAway(value.timeTable);
            }
            if(x<=0) {
                SharedPreferencesHelper.removeAlarmJson(this, stringValue);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            // app icon in action bar clicked; goto parent activity.
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateAlarms() {

        ArrayList<String> tempArray = SharedPreferencesHelper.getAlarms(this);
        Gson gson = new Gson();
        mAlarms.clear();
        for(int i = 0; i < tempArray.size();i++) {
            Values value = gson.fromJson(tempArray.get(i), Values.class);
            mAlarms.add(value);
        }

        mAdapter.notifyDataSetChanged();

    }


    public void onClick(View v) {
        if(v.getId()==R.id.confirm_timer) {
            Date alarmTime;
            if(!mAlarmValues.realTime.contains("null")) {
                alarmTime = DateUtils.convertToDate(mAlarmValues.realTime);
            }
            else {
                alarmTime = DateUtils.convertToDate(mAlarmValues.timeTable);
            }
            //set pending and add the model to memory for ability to cancel

            CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.main_layout);

            AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            Gson gson = new Gson();
            String jsonValues = gson.toJson(mAlarmValues);
            intent.putExtra(PTVConstants.JSON_VALUES, (Parcelable) mAlarmValues);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            if(SharedPreferencesHelper.isAlarmActivated(this, jsonValues)) {
                SharedPreferencesHelper.removeAlarmJson(this, jsonValues);
                alarmMgr.cancel(pendingIntent);
                final Snackbar snackBar = Snackbar.make(layout, "Alarm has been removed for stop " + mAlarmValues.platform.stop.stopId, Snackbar.LENGTH_LONG);
                snackBar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        snackBar.dismiss();
                    }
                });
                snackBar.show();

            }
            else {
                SharedPreferencesHelper.saveAlarmJson(jsonValues, this);
                alarmMgr.set(AlarmManager.RTC_WAKEUP, alarmTime.getTime(), pendingIntent);
                final Snackbar snackBar = Snackbar.make(layout, "Alarm has been set for stop " +  mAlarmValues.platform.stop.stopId, Snackbar.LENGTH_LONG);
                snackBar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        snackBar.dismiss();
                    }
                });
                snackBar.show();
            }
            timerCardView.setVisibility(View.INVISIBLE);
            updateAlarms();
        }
    }

    @Override
    public void onBackPressed() {
        if(timerCardView.getVisibility()==View.VISIBLE) {
            timerCardView.setVisibility(View.INVISIBLE);
        }
        else {
            super.onBackPressed();
        }
    }

}

