package com.example.adam.pubtrans.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.SlidingTabLayout;
import com.example.adam.pubtrans.adapters.DrawerListAdapter;
import com.example.adam.pubtrans.adapters.MyFragmentPagerAdapter;
import com.example.adam.pubtrans.fragments.NearMeListFragment;
import com.example.adam.pubtrans.fragments.TramSimulatorFragment;
import com.example.adam.pubtrans.interfaces.Callback;
import com.example.adam.pubtrans.interfaces.IFabAnimate;
import com.example.adam.pubtrans.interfaces.IPubActivity;
import com.example.adam.pubtrans.interfaces.IResults;
import com.example.adam.pubtrans.models.NavItem;
import com.example.adam.pubtrans.utils.ImageUtils;
import com.example.adam.pubtrans.utils.SharedPreferencesHelper;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdate;
import com.example.adam.pubtrans.models.Disruption;
import com.example.adam.pubtrans.models.NearMeResult;
import com.example.adam.pubtrans.models.Values;
import com.example.adam.pubtrans.utils.PTVConstants;
import com.example.adam.pubtrans.utils.WebApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
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
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements IPubActivity, IFabAnimate, OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener, Callback<ArrayList<NearMeResult>>, GoogleApiClient.ConnectionCallbacks, GoogleMap.OnInfoWindowClickListener, View.OnClickListener  {
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    ArrayList<NearMeResult> nearMeResults;
    ArrayList<Disruption>  disruptionsResults;
    ArrayList<NearMeResult> filteredResults;

    private ArrayList<Marker> markerArrayList;
    ArrayList<Fragment> fragments;
    private GoogleMap googleMap;
    public final static String TAG = "MainActivity";
    private ArrayList<String> filter;
    private boolean firstLoad;
    private boolean mapIsReady = false;
    private ActionBarDrawerToggle mDrawerToggle;

    @Bind(R.id.pager) ViewPager mViewPager;
    @Bind(R.id.my_awesome_toolbar) Toolbar toolbar;
    @Bind(R.id.tabs) SlidingTabLayout tabs;
    @Bind(R.id.navList) ListView mDrawerList;
    @Bind(R.id.drawerPane) RelativeLayout mDrawerPane;
    @Bind(R.id.drawerLayout) DrawerLayout mDrawerLayout;

    @Bind(R.id.menu1) FloatingActionMenu fab;

    @Bind(R.id.fab1) FloatingActionButton fab1;
    @Bind(R.id.fab2) FloatingActionButton fab2;
    @Bind(R.id.fab3) FloatingActionButton fab3;

    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);


        setTitle("Near me");
        firstLoad = true;
        disruptionsResults = new ArrayList<>();
        nearMeResults = new ArrayList<>();
        fragments = (ArrayList<Fragment>) getFragments();
        filter = new ArrayList<String>();

        filter.add("train");
        filter.add("tram");
        filter.add("bus");

        mNavItems.add(new NavItem("Search", "Search for stops", R.drawable.ic_search_black_48dp));
        mNavItems.add(new NavItem("Home", "Keep your favourites here", R.drawable.ic_home_black_48dp));
        mNavItems.add(new NavItem("Disruptions", "Information about affected transport", R.drawable.ic_report_problem_black_48dp));
        mNavItems.add(new NavItem("Alarms", "View your transport alarms", R.drawable.ic_alarm_black_48dp));

        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d(TAG, "onDrawerClosed: " + getTitle());

                invalidateOptionsMenu();
            }
        };

        filteredResults = new ArrayList<>();




        mDrawerLayout.setDrawerListener(mDrawerToggle);

        MyFragmentPagerAdapter fragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(fragmentPagerAdapter);

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
        tabs.setViewPager(mViewPager);


        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);

        fab1.setSelected(true);
        fab2.setSelected(true);
        fab3.setSelected(true);

        markerArrayList = new ArrayList<>();
        ((SupportMapFragment)fragments.get(0)).getExtendedMapAsync(this);
        buildGoogleApiClient();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mapIsReady) {
            filterResults();
        }
        growFab();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    /*
* Called when a particular item from the navigation drawer
* is selected.
* */
    private void selectItemFromDrawer(int position) {
        switch(position) {
            case 0: {
                Intent intent = new Intent(this, SearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            }

            case 2: {
                Intent intent = new Intent(this, DisruptionsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;

            }

            case 3: {
                Intent intent = new Intent(this, AlarmsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            }

            default: {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            }

        }
    }


    private List<Fragment> getFragments(){

        List<Fragment> fList = new ArrayList<>();

        fList.add(SupportMapFragment.newInstance());
        fList.add(NearMeListFragment.newInstance("Fragment 2"));
        fList.add(TramSimulatorFragment.newInstance("Fragment 3"));

        return fList;

    }

    public ArrayList<Disruption> getDisruptionsResults() {
        return disruptionsResults;
    }
    public ArrayList<NearMeResult> getNearMeResults() {
        return filteredResults;
    }
    public ArrayList<Values> getAlarms() {return null;}


    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = ((SupportMapFragment)fragments.get(0)).getExtendedMap();
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setMyLocationEnabled(true);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(-37.643099, 144.754956));
        builder.include(new LatLng(-38.434046, 145.595909));
        LatLngBounds bounds = builder.build();
        int padding = 10; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.moveCamera(cu);
        mapIsReady = true;
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

        if (id == R.id.action_refresh) {
            Fragment fragment = fragments.get(1);
            if(fragment!=null) {
                ((IResults) fragment).refresh();
            }
            if (mLastLocation != null) {
                LatLng loc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                try {
                    WebApi.getNearMe(loc, this);
                }
                catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
            return true;
        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if(id == android.R.id.home){
            // app icon in action bar clicked; goto parent activity.
            onBackPressed();
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


    public void success(ArrayList<NearMeResult> nearMeResults) {
        this.nearMeResults = nearMeResults;
        runOnUiThread(new Runnable()
        {

            public void run()
            {
                filterResults();
            }
        });
    }


    public void onClick(View v) {
        if(v instanceof FloatingActionButton) {
            FloatingActionButton fab = (FloatingActionButton) v;
            fab.setSelected(!fab.isSelected());
            if(fab.isSelected()) {
                if(!filter.contains((String) fab.getTag())) {
                    filter.add((String)fab.getTag());
                }
            }
            else {
                if(filter.contains((String)fab.getTag())) {
                    filter.remove((String)fab.getTag());
                }
            }
            filterResults();
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        shrinkFab();
        Intent intent = new Intent(this, SecondaryActivity.class);
        NearMeResult nearMeResult = marker.getData();
        Gson gson = new Gson();
        String jsonNearMeResult = gson.toJson(nearMeResult);
        intent.putExtra(PTVConstants.JSON_NEARMERESULT, jsonNearMeResult);
        startActivity(intent);
    }

    public void filterResults() {
        if(filteredResults==null) {
            filteredResults = new ArrayList<>();
        }
        filteredResults.clear();
        ArrayList<NearMeResult> favourites = SharedPreferencesHelper.getFavouriteStops(this);
        filteredResults.add(new NearMeResult("Favourites"));
        if(favourites!=null&&favourites.size()>0) {

            filteredResults.addAll(favourites);
        }
        filteredResults.add(new NearMeResult("Stops Nearby"));
        markerArrayList.clear();
        googleMap.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(NearMeResult result : nearMeResults) {
            if (filter.contains(result.result.transportType)) {
                filteredResults.add(result);
                LatLng loc = new LatLng(result.result.latitude, result.result.longitude);
                Marker marker= googleMap.addMarker(new MarkerOptions().position(loc).title(result.result.locationName + " " + result.result.transportType).snippet(result.result.suburb).icon(ImageUtils.getTransportPinDescriptor(result.result.transportType)));
                marker.setData(result);
                builder.include(marker.getPosition());
                markerArrayList.add(marker);
            }
        }
        Log.e("MainActivity", Integer.toString(filteredResults.size()));
        ((IResults<NearMeResult>) fragments.get(1)).setResults((ArrayList<NearMeResult>) filteredResults.clone());
        if(firstLoad) {
            try {
                LatLngBounds bounds = builder.build();
                int padding = 10; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                googleMap.animateCamera(cu);
                firstLoad = false;
            }catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    public void shrinkFab() {
        Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_shrink);
        fab.setMenuButtonHideAnimation(animScale);
        fab.hideMenuButton(true);
    }

    public void growFab() {
        Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_grow);
        fab.setVisibility(View.VISIBLE);
        fab.setMenuButtonShowAnimation(animScale);
        fab.showMenuButton(true);
    }
}
