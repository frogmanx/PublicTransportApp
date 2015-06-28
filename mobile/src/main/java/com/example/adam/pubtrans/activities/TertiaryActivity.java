package com.example.adam.pubtrans.activities;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.SlidingTabLayout;
import com.example.adam.pubtrans.adapters.MyFragmentPagerAdapter;
import com.example.adam.pubtrans.fragments.StopsListFragment;
import com.example.adam.pubtrans.fragments.ValuesListFragment;
import com.example.adam.pubtrans.interfaces.IResults;
import com.example.adam.pubtrans.interfaces.IWebApiResponse;
import com.example.adam.pubtrans.models.BroadNextDeparturesResult;
import com.example.adam.pubtrans.models.Disruption;
import com.example.adam.pubtrans.models.NearMeResult;
import com.example.adam.pubtrans.models.Stop;
import com.example.adam.pubtrans.models.Values;
import com.example.adam.pubtrans.utils.DateUtils;
import com.example.adam.pubtrans.utils.ImageUtils;
import com.example.adam.pubtrans.utils.PTVConstants;
import com.example.adam.pubtrans.utils.WebApi;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 31/05/2015.
 */
public class TertiaryActivity extends BaseActivity implements IWebApiResponse, GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback {
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    FragmentManager fragmentManager;
    ArrayList<Stop> stopsList;
    ArrayList<Values> valuesList;
    public static double THRESHOLD = 240000;

    private ArrayList<Marker> markerArrayList;
    ArrayList<Fragment> fragments;
    private GoogleMap googleMap;
    public final static String TAG = "MainActivity";
    ViewPager mViewPager;
    SlidingTabLayout tabs;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tertiary_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        setTitle("Stopping Pattern");

        stopsList = new ArrayList<>();
        valuesList = new ArrayList<>();

        mViewPager = (ViewPager) findViewById(R.id.pager);
        fragments = (ArrayList<Fragment>) getFragments();


        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.secondary);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(mViewPager);


        markerArrayList = new ArrayList<>();
        //((SupportMapFragment)fragments.get(0)).getMapAsync(this);
        buildGoogleApiClient();



        ((SupportMapFragment)fragments.get(0)).getExtendedMapAsync(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentManager = getSupportFragmentManager();

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
        googleMap = ((SupportMapFragment)fragments.get(0)).getExtendedMap();
        googleMap.setMyLocationEnabled(true);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(-37.643099, 144.754956));
        builder.include(new LatLng(-38.434046, 145.595909));
        LatLngBounds bounds = builder.build();
        int padding = 10; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.moveCamera(cu);
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
            Fragment fragment = fragments.get(1);
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
        return stopsList;
    }
    public ArrayList<Values> getValuesList() {
        return valuesList;
    }


    @Override
    public void stopsOnLineResponse(final ArrayList<Stop>  stopResults) {
        this.stopsList = stopResults;
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                ((IResults) fragments.get(1)).setResults(stopResults);
                if(stopResults!=null && stopResults.size() > 1 ) {
                    googleMap.clear();
                    markerArrayList.clear();
                }
                for(Stop object: stopResults){
                    LatLng loc = new LatLng(object.latitude, object.longitude);
                    markerArrayList.add(googleMap.addMarker(new MarkerOptions().position(loc).title(object.locationName + " " + object.transportType).icon(ImageUtils.getTransportPinDescriptor(object.transportType))));
                    if(googleMap != null){
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                    }
                }
            }
        });
    }
    public void valuesResponse(final ArrayList<Values> valuesResults) {
        this.valuesList = valuesResults;
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                ((IResults) fragments.get(1)).setResults(valuesList);
                if(valuesResults!=null && valuesResults.size() > 1 ) {
                    googleMap.clear();
                    markerArrayList.clear();
                }

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for(Values object: valuesList){
                    LatLng loc = new LatLng(object.platform.stop.latitude, object.platform.stop.longitude);
                    Marker marker;
                    if(!object.realTime.contentEquals("null")) {
                        float y = DateUtils.getAlphaFromTime(object.realTime, THRESHOLD);
                        marker = googleMap.addMarker(new MarkerOptions().alpha(y).position(loc).title(object.platform.stop.locationName).icon(ImageUtils.getTransportPinDescriptor(object.run.transportType)).snippet("R " + y + " " + DateUtils.convertToContext(object.realTime, false)));
                        markerArrayList.add(marker);
                    }
                    else {
                        marker = googleMap.addMarker(new MarkerOptions().position(loc).title(object.platform.stop.locationName).icon(ImageUtils.getTransportPinDescriptor(object.run.transportType)).snippet("T " + DateUtils.convertToContext(object.timeTable, false)));
                        markerArrayList.add(marker);
                    }
                    builder.include(marker.getPosition());
                    try {
                        if(googleMap != null) {
                            LatLngBounds bounds = builder.build();
                            int padding = 10; // offset from edges of the map in pixels
                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                            googleMap.animateCamera(cu);
                        }

                    }catch (IllegalStateException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }

}
