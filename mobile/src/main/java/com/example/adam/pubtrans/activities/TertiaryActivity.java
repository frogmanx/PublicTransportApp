package com.example.adam.pubtrans.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.SlidingTabLayout;
import com.example.adam.pubtrans.adapters.MyFragmentPagerAdapter;
import com.example.adam.pubtrans.fragments.ValuesListFragment;
import com.example.adam.pubtrans.interfaces.Callback;
import com.example.adam.pubtrans.interfaces.IAddTimer;
import com.example.adam.pubtrans.interfaces.IFabAnimate;
import com.example.adam.pubtrans.interfaces.IResults;
import com.example.adam.pubtrans.models.Stop;
import com.example.adam.pubtrans.models.Values;
import com.example.adam.pubtrans.receivers.AlarmReceiver;
import com.example.adam.pubtrans.utils.DateUtils;
import com.example.adam.pubtrans.utils.ImageUtils;
import com.example.adam.pubtrans.utils.PTVConstants;
import com.example.adam.pubtrans.utils.SharedPreferencesHelper;
import com.example.adam.pubtrans.utils.WebApi;
import com.example.adam.pubtrans.views.SelectableFloatingActionButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.OnMapReadyCallback;
import com.androidmapsextensions.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.androidmapsextensions.Marker;
import com.androidmapsextensions.MarkerOptions;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Adam on 31/05/2015.
 */
public class TertiaryActivity extends BaseActivity implements Callback<ArrayList<Values>>, GoogleMap.OnInfoWindowClickListener, GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, IFabAnimate, IAddTimer {

    public final static String TAG = "MainActivity";

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    FragmentManager mFragmentManager;
    ArrayList<Stop> mStopsList;
    ArrayList<Values> mValuesList;
    private Values mAlarmValues;
    public static double THRESHOLD = 240000;

    ArrayList<Marker> mMarkerArrayList;
    ArrayList<Fragment> mFragments;
    GoogleMap mGoogleMap;

    @Bind(R.id.pager) ViewPager viewPager;
    @Bind(R.id.my_awesome_toolbar) Toolbar toolbar;
    @Bind(R.id.tabs) SlidingTabLayout tabs;
    @Bind(R.id.card_view) CardView cardView;
    @Bind(R.id.timer_card_view) CardView timerCardView;
    @Bind(R.id.fab) SelectableFloatingActionButton fab;

    int cx;
    int cy;
    float radius;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tertiary_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle("Stopping Pattern");

        mStopsList = new ArrayList<>();
        mValuesList = new ArrayList<>();

        mFragments = (ArrayList<Fragment>) getFragments();


        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments);
        viewPager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.secondary);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(viewPager);


        mMarkerArrayList = new ArrayList<>();
        //((SupportMapFragment)fragments.get(0)).getMapAsync(this);
        buildGoogleApiClient();



        ((SupportMapFragment)mFragments.get(0)).getExtendedMapAsync(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFragmentManager = getSupportFragmentManager();

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        growFab();
    }

    public void revealView(View view) {

        final View myView = view;
        cx = (view.getLeft() + view.getRight()) / 2;
        cy = (view.getBottom() - view.getTop()) / 2;
        radius = Math.max(cardView.getWidth(), cardView.getHeight()) * 2.0f;

        if (cardView.getVisibility() == View.INVISIBLE) {
            cardView.setVisibility(View.VISIBLE);
            ViewAnimationUtils.createCircularReveal(cardView, cx, cy, 0, radius).start();
        } else {
            Animator reveal = ViewAnimationUtils.createCircularReveal(
                    cardView, cx, cy, radius, 0);
            reveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    myView.setVisibility(View.INVISIBLE);
                }
            });
            reveal.start();
        }
    }

    public void showAddTimerView(View view, Values values) {

        Gson gson = new Gson();
        String jsonValues = gson.toJson(values);
        if(SharedPreferencesHelper.isAlarmActivated(this, jsonValues)) {
            ((TextView) timerCardView.findViewById(R.id.card_text)).setText("Remove Alarm");
        }
        else {
            ((TextView) timerCardView.findViewById(R.id.card_text)).setText("Activate Alarm");
        }

        mAlarmValues = values;
        final View myView = view;
        cx = (view.getLeft() + view.getRight()) / 2;
        cy = (view.getTop() + view.getBottom()) / 2;
        radius = Math.max(timerCardView.getWidth(), timerCardView.getHeight()) * 2.0f;

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

    private List<Fragment> getFragments(){

        List<Fragment> fList = new ArrayList<>();

        fList.add(SupportMapFragment.newInstance());
        fList.add(ValuesListFragment.newInstance("Fragment 2"));

        return fList;

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = ((SupportMapFragment)mFragments.get(0)).getExtendedMap();
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setOnInfoWindowClickListener(this);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(-37.643099, 144.754956));
        builder.include(new LatLng(-38.434046, 145.595909));
        LatLngBounds bounds = builder.build();
        int padding = 10; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mGoogleMap.moveCamera(cu);
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            try {
                Bundle bundle = getIntent().getExtras();
                try {
                    WebApi.getStoppingPattern(bundle.getString(PTVConstants.TRANSPORT_TYPE), bundle.getInt(PTVConstants.RUN_ID), bundle.getInt(PTVConstants.STOP_ID), this);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.secondary_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            Fragment fragment = mFragments.get(1);
            if(fragment!=null) {
                ((IResults) fragment).refresh();
            }
            Bundle bundle = getIntent().getExtras();
            try {
                WebApi.getStoppingPattern(bundle.getString(PTVConstants.TRANSPORT_TYPE), bundle.getInt(PTVConstants.RUN_ID), bundle.getInt(PTVConstants.STOP_ID), this);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        if(id == android.R.id.home){
            // app icon in action bar clicked; goto parent activity.
            onBackPressed();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public ArrayList<Stop> getStopsList() {
        return mStopsList;
    }
    public ArrayList<Values> getValuesList() {
        return mValuesList;
    }


    public void shrinkFab() {
        Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_shrink);
        fab.startAnimation(animScale);
    }

    public void growFab() {
        Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_grow);
        fab.startAnimation(animScale);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Values values = marker.getData();
        Log.e(TAG, "Info Window Clicked");
        showAddTimerView(cardView, values);
    }


    public void success(final ArrayList<Values> valuesResults) {
        //filter out before time
        ArrayList<Values> myResults = new ArrayList<>();
        if(valuesResults.size()>0 && valuesResults.get(0).realTime!=null&&valuesResults.get(0).realTime!=null) {
            for(int i = 0; i < valuesResults.size();i++) {
                if(valuesResults.get(i).realTime!=null) {
                    double x = DateUtils.convertToMSAway(valuesResults.get(i).realTime);
                    if(x>0) {
                        myResults.add(valuesResults.get(i));
                    }
                }
            }
        }
        else {
            for(int i = 0; i < valuesResults.size();i++) {
                double x = DateUtils.convertToMSAway(valuesResults.get(i).timeTable);
                if(x>0) {
                    myResults.add(valuesResults.get(i));
                }
            }
        }
        this.mValuesList = myResults;

        runOnUiThread(new Runnable()
        {
            public void run()
            {
                ((IResults) mFragments.get(1)).setResults(mValuesList);
                if(valuesResults!=null && valuesResults.size() > 1 ) {
                    mGoogleMap.clear();
                    mMarkerArrayList.clear();
                }

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for(Values object: mValuesList){
                    LatLng loc = new LatLng(object.platform.stop.latitude, object.platform.stop.longitude);
                    Marker marker;
                    if(object.realTime!=null) {
                        float y = DateUtils.getAlphaFromTime(object.realTime, THRESHOLD);
                        double x = DateUtils.convertToMSAway(object.realTime);

                        if(x>0) {
                            // marker = googleMap.addMarker(new MarkerOptions().alpha(y).position(loc).title(object.platform.stop.locationName).icon(ImageUtils.getTransportPinDescriptor(object.run.transportType)).snippet("R " + y + " " + DateUtils.convertToContext(object.realTime, false)));
                            marker = mGoogleMap.addMarker(new MarkerOptions().position(loc).title(object.platform.stop.locationName).icon(ImageUtils.getTransportPinDescriptor(object.run.transportType)).snippet("R " + y + " " + DateUtils.convertToContext(object.realTime, false)));
                            marker.setData(object);

                            mMarkerArrayList.add(marker);
                            builder.include(marker.getPosition());
                        }

                    }
                    else {
                        double x = DateUtils.convertToMSAway(object.timeTable);
                        if(x>0) {
                            marker = mGoogleMap.addMarker(new MarkerOptions().position(loc).title(object.platform.stop.locationName).icon(ImageUtils.getTransportPinDescriptor(object.run.transportType)).snippet("T " + DateUtils.convertToContext(object.timeTable, false)));
                            marker.setData(object);
                            mMarkerArrayList.add(marker);
                            builder.include(marker.getPosition());
                        }
                    }

                    try {
                        if(mGoogleMap != null) {
                            LatLngBounds bounds = builder.build();
                            int padding = 10; // offset from edges of the map in pixels
                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                            mGoogleMap.moveCamera(cu);
                        }

                    }catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



    public void onClick(View v) {
        if(v.getId()==R.id.confirm_timer) {
            Date alarmTime;
            if(mAlarmValues.realTime!=null) {
                alarmTime = DateUtils.convertToDate(mAlarmValues.realTime);
            }
            else {
                alarmTime = DateUtils.convertToDate(mAlarmValues.timeTable);
            }
            //set pending and add the model to memory for ability to cancel


            AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            Gson gson = new Gson();
            String jsonValues = gson.toJson(mAlarmValues);
            intent.putExtra(PTVConstants.JSON_VALUES, jsonValues);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            if(SharedPreferencesHelper.isAlarmActivated(this, jsonValues)) {
                SharedPreferencesHelper.removeAlarmJson(this, jsonValues);
                alarmMgr.cancel(pendingIntent);
                final Snackbar snackBar = Snackbar.make(fab, "Alarm has been removed for stop " + mAlarmValues.platform.stop.stopId, Snackbar.LENGTH_LONG);
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
                final Snackbar snackBar = Snackbar.make(fab, "Alarm has been set for stop " +  mAlarmValues.platform.stop.stopId, Snackbar.LENGTH_LONG);
                snackBar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        snackBar.dismiss();
                    }
                });
                snackBar.show();
            }
            Animator reveal = ViewAnimationUtils.createCircularReveal(timerCardView, cx, cy, radius, 0);
            reveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    timerCardView.setVisibility(View.INVISIBLE);
                }
            });
            reveal.start();
        }
        else {
            growFab();
            final Snackbar snackBar = Snackbar.make(fab, "Your feedback has been collected.", Snackbar.LENGTH_LONG);
            snackBar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    snackBar.dismiss();
                }
            });
            snackBar.show();
            Animator reveal = ViewAnimationUtils.createCircularReveal(cardView, cx, cy, radius, 0);
            reveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    cardView.setVisibility(View.INVISIBLE);
                }
            });
            reveal.start();
        }
    }

    @Override
    public void onBackPressed() {
        if(cardView.getVisibility()==View.VISIBLE) {
            Animator reveal = ViewAnimationUtils.createCircularReveal(cardView, cx, cy, radius, 0);
            reveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    cardView.setVisibility(View.INVISIBLE);
                }
            });
            reveal.start();
            growFab();
        }
        else if(timerCardView.getVisibility()==View.VISIBLE) {
            Animator reveal = ViewAnimationUtils.createCircularReveal(timerCardView, cx, cy, radius, 0);
            reveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    timerCardView.setVisibility(View.INVISIBLE);
                }
            });
            reveal.start();
        }
        else {
            shrinkFab();
            super.onBackPressed();
        }
    }
}
