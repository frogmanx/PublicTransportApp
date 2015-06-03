package com.example.adam.pubtrans.activities;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.SlidingTabLayout;
import com.example.adam.pubtrans.adapters.MyFragmentPagerAdapter;
import com.example.adam.pubtrans.fragments.DisruptionsFragment;
import com.example.adam.pubtrans.fragments.NearMeListFragment;
import com.example.adam.pubtrans.interfaces.IResults;
import com.example.adam.pubtrans.interfaces.IWebApiResponse;
import com.example.adam.pubtrans.models.BroadNextDeparturesResult;
import com.example.adam.pubtrans.models.Disruption;
import com.example.adam.pubtrans.models.DisruptionsResult;
import com.example.adam.pubtrans.models.NearMeResult;
import com.example.adam.pubtrans.models.Stop;
import com.example.adam.pubtrans.models.Values;
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



public class MainActivity extends FragmentActivity  implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener, IWebApiResponse, GoogleApiClient.ConnectionCallbacks {
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<NearMeResult> nearMeResults;
    ArrayList<Disruption>  disruptionsResults;

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
        setContentView(R.layout.activity_main);

        disruptionsResults = new ArrayList<>();
        nearMeResults = new ArrayList<>();
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
        ((SupportMapFragment)fragments.get(0)).getMapAsync(this);
        buildGoogleApiClient();

        try {
            WebApi.getDisruptions(this);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    private List<Fragment> getFragments(){

        List<Fragment> fList = new ArrayList<>();

        fList.add(SupportMapFragment.newInstance());
        fList.add(NearMeListFragment.newInstance("Fragment 2"));
        fList.add(DisruptionsFragment.newInstance("Fragment 3"));

        return fList;

    }

    public ArrayList<Disruption> getDisruptionsResults() {
        return disruptionsResults;
    }
    public ArrayList<NearMeResult> getNearMeResults() {
        return nearMeResults;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onMyLocationChange(Location location) {
        /*String url = "";
        try {
           // url = WebApi.buildTTAPIURL("http://timetableapi.ptv.vic.gov.au", "65a71c46-0343-11e5-9cc2-02f9e320053a", "/v2/nearme/latitude/-37.82392/longitude/144.9462017", 1000460);
            url = WebApi.buildTTAPIURL("http://timetableapi.ptv.vic.gov.au", "65a71c46-0343-11e5-9cc2-02f9e320053a", "/v2/nearme/latitude/-37.82392/longitude/144.9462017", 1000460);
        }catch (Exception e) {
            e.printStackTrace();
        }
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        WebApi.getNearMe()
        mMarker = googleMap.addMarker(new MarkerOptions().position(loc));
        if(googleMap != null){
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
        }*/
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        try {
            WebApi.getNearMe(loc, this);
        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            LatLng loc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            try {
                WebApi.getNearMe(loc, this);
            }
            catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void nearMeResponse(final ArrayList<NearMeResult> nearMeResults) {
        this.nearMeResults = nearMeResults;
        runOnUiThread(new Runnable()
        {

            public void run()
            {
                ((IResults) fragments.get(1)).setResults(nearMeResults);
                for(NearMeResult object: nearMeResults){
                    LatLng loc = new LatLng(object.latitude, object.longitude);
                    markerArrayList.add(googleMap.addMarker(new MarkerOptions().position(loc).title(object.locationName + " " + object.transportType)));
                    if(googleMap != null){
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                    }
                }
            }
        });

    }

    public void broadNextDeparturesResponse(final ArrayList<BroadNextDeparturesResult> broadNextDeparturesResponse) {

    }

    public void disruptionsResponse(final ArrayList<Disruption>  disruptionsResults) {
        this.disruptionsResults = disruptionsResults;
        runOnUiThread(new Runnable()
        {

            public void run()
            {
                ((IResults) fragments.get(2)).setResults(disruptionsResults);
            }
        });
    }

    public void stopsOnLineResponse(final ArrayList<Stop>  stopResults) {

    }

    public void valuesResponse(ArrayList<Values> valuesResults) {

    }
}
