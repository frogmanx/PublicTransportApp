package com.example.adam.pubtrans.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.adapters.DisruptionsAdapter;
import com.example.adam.pubtrans.adapters.MyFragmentPagerAdapter;
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
import com.example.adam.pubtrans.utils.WebApi;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Adam on 4/07/2015.
 */
public class AlarmsActivity extends BaseActivity implements IPubActivity,IAddTimer {

    ViewPager mViewPager;
    ArrayList<Fragment> fragments;

    ArrayList<Values>  alarms;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    CardView timerCardView;

    private Values alarmValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disruptions);

        Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);

        cleanseAlarms();

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Alarms");

        setTitle("Alarms");
        alarms = new ArrayList<>();
        mViewPager = (ViewPager) findViewById(R.id.pager);
        fragments = (ArrayList<Fragment>) getFragments();

        MyFragmentPagerAdapter fragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        // mViewPager.setAdapter(fragmentPagerAdapter);

        mRecyclerView = (RecyclerView) findViewById(R.id.scrollableview);


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        timerCardView = (CardView) findViewById(R.id.timer_card_view);

        mAdapter = new ValuesAdapter(alarms);
        mRecyclerView.setAdapter(mAdapter);

        updateAlarms();

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
    public ArrayList<Values> getAlarms() {return alarms;}

    public void showAddTimerView(View view, Values values) {

        Gson gson = new Gson();
        String jsonValues = gson.toJson(values);
        if(SharedPreferencesHelper.isAlarmActivated(this, jsonValues)) {
            ((TextView) timerCardView.findViewById(R.id.card_text)).setText("Remove Alarm");
        }
        else {
            ((TextView) timerCardView.findViewById(R.id.card_text)).setText("Activate Alarm");
        }

        alarmValues = values;
        final View myView = view;
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

    public void updateAlarms() {

        ArrayList<String> tempArray = SharedPreferencesHelper.getAlarms(this);
        Gson gson = new Gson();
        alarms.clear();
        for(int i = 0; i < tempArray.size();i++) {
            Values value = gson.fromJson(tempArray.get(i), Values.class);
            alarms.add(value);
        }

        mAdapter.notifyDataSetChanged();

    }


    public void onClick(View v) {
        if(v.getId()==R.id.confirm_timer) {
            Date alarmTime;
            if(!alarmValues.realTime.contains("null")) {
                alarmTime = DateUtils.convertToDate(alarmValues.realTime);
            }
            else {
                alarmTime = DateUtils.convertToDate(alarmValues.timeTable);
            }
            //set pending and add the model to memory for ability to cancel

            CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.main_layout);

            AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            Gson gson = new Gson();
            String jsonValues = gson.toJson(alarmValues);
            intent.putExtra(PTVConstants.JSON_VALUES, jsonValues);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            if(SharedPreferencesHelper.isAlarmActivated(this, jsonValues)) {
                SharedPreferencesHelper.removeAlarmJson(this, jsonValues);
                alarmMgr.cancel(pendingIntent);
                final Snackbar snackBar = Snackbar.make(layout, "Alarm has been removed for stop " + alarmValues.platform.stop.stopId, Snackbar.LENGTH_LONG);
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
                final Snackbar snackBar = Snackbar.make(layout, "Alarm has been set for stop " +  alarmValues.platform.stop.stopId, Snackbar.LENGTH_LONG);
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

