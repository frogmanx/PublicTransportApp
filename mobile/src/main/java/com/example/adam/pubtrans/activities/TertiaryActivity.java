package com.example.adam.pubtrans.activities;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
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
import com.example.adam.pubtrans.utils.PTVConstants;
import com.example.adam.pubtrans.utils.WebApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 31/05/2015.
 */
public class TertiaryActivity extends FragmentActivity implements IWebApiResponse, GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback {
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



        ((SupportMapFragment)fragments.get(0)).getMapAsync(this);

        getActionBar().setDisplayHomeAsUpEnabled(true);

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
        googleMap = ((SupportMapFragment)fragments.get(0)).getMap();
        googleMap.setMyLocationEnabled(true);
        map.addMarker(new MarkerOptions()
                .position(new LatLng(-37.82392, 144.9462017))
                .title("Hello world"));
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

        return super.onOptionsItemSelected(item);
    }

    public ArrayList<Stop> getStopsList() {
        return stopsList;
    }
    public ArrayList<Values> getValuesList() {
        return valuesList;
    }

    public void broadNextDeparturesResponse(final ArrayList<BroadNextDeparturesResult> broadNextDeparturesResults) {



    }

    public void nearMeResponse(ArrayList<NearMeResult> nearMeResults) {

    }

    public void disruptionsResponse(final ArrayList<Disruption>  disruptionsResults) {

    }

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
                    markerArrayList.add(googleMap.addMarker(new MarkerOptions().position(loc).title(object.locationName + " " + object.transportType)));
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
                for(Values object: valuesList){
                    LatLng loc = new LatLng(object.platform.stop.latitude, object.platform.stop.longitude);
                    if(!object.realTime.contentEquals("null")) {
                        float y = DateUtils.getAlphaFromTime(object.realTime, THRESHOLD);
                        markerArrayList.add(googleMap.addMarker(new MarkerOptions().alpha(y).position(loc).title(object.platform.stop.locationName).snippet("R " + y + " " + DateUtils.convertToContext(object.realTime, false))));
                    }
                    else {
                        markerArrayList.add(googleMap.addMarker(new MarkerOptions().position(loc).title(object.platform.stop.locationName).snippet("T " + DateUtils.convertToContext(object.timeTable, false))));
                    }
                    if(googleMap != null){
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                    }
                }
            }
        });
    }

}
