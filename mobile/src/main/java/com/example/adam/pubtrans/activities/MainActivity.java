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

import com.example.adam.pubtrans.Crime;
import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.SlidingTabLayout;
import com.example.adam.pubtrans.adapters.MyFragmentPagerAdapter;
import com.example.adam.pubtrans.fragments.MainActivityFragment;
import com.example.adam.pubtrans.interfaces.IWebApiResponse;
import com.example.adam.pubtrans.models.NearMeResult;
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

    private ArrayList<Crime> persons;
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



        /*final com.rey.material.widget.FloatingActionButton fab = (com.rey.material.widget.FloatingActionButton) findViewById(R.id.fab);
        ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                // Or read size directly from the view's width/height
                int size = getResources().getDimensionPixelSize(R.dimen.fab_size);
                outline.setOval(0, 0, size, size);
            }
        };
        fab.setOutlineProvider(viewOutlineProvider);


        // define a click listener
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                // create the transition animation - the images in the layouts
                // of both activities are defined with android:transitionName="robot"
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation((Activity) view.getContext(), fab, "robot");
                // start the new activity
                startActivity(intent, options.toBundle());
            }
        });
        */
    }


    private List<Fragment> getFragments(){

        List<Fragment> fList = new ArrayList<>();

        fList.add(SupportMapFragment.newInstance());
        fList.add(MainActivityFragment.newInstance("Fragment 3"));

        return fList;

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
        runOnUiThread(new Runnable()
        {

            public void run()
            {
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

    public void nearMeResponse(String nearMeResults) {
        Log.e(TAG, nearMeResults);
        Toast.makeText(this, nearMeResults, Toast.LENGTH_LONG).show();
        /*LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        mMarker = googleMap.addMarker(new MarkerOptions().position(loc));
        if(googleMap != null){
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
        }*/
    }








}
