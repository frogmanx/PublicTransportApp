package com.example.adam.pubtrans.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements NearMeListFragment.ILocation, IPubActivity,
        IFabAnimate, OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener, Callback<ArrayList<NearMeResult>>,
        GoogleApiClient.ConnectionCallbacks, GoogleMap.OnInfoWindowClickListener, View.OnClickListener  {
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    ArrayList<NearMeResult> mNearMeResults;
    ArrayList<Disruption>  mDisruptionsResults;
    ArrayList<NearMeResult> filteredResults;

    private ArrayList<Marker> mMarkerArrayList;
    ArrayList<Fragment> mFragments;
    private GoogleMap mGoogleMap;
    public final static String TAG = "MainActivity";
    private ArrayList<String> mFilter;
    private boolean mFirstLoad;
    private boolean mMapIsReady = false;
    private ActionBarDrawerToggle mDrawerToggle;

    @Bind(R.id.pager) ViewPager viewPager;
    @Bind(R.id.my_awesome_toolbar) Toolbar toolbar;
    @Bind(R.id.tabs) SlidingTabLayout tabs;
    @Bind(R.id.navList) ListView drawerList;
    @Bind(R.id.drawerLayout) DrawerLayout drawerLayout;

    @Bind(R.id.menu1) FloatingActionMenu fab;

    @Bind(R.id.fab1) FloatingActionButton fab1;
    @Bind(R.id.fab2) FloatingActionButton fab2;
    @Bind(R.id.fab3) FloatingActionButton fab3;

    ArrayList<NavItem> mNavItems = new ArrayList<>();

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);


        setTitle("Near me");
        mFirstLoad = true;
        mDisruptionsResults = new ArrayList<>();
        mNearMeResults = new ArrayList<>();
        mFragments = (ArrayList<Fragment>) getFragments();
        mFilter = new ArrayList<>();

        mFilter.add("train");
        mFilter.add("tram");
        mFilter.add("bus");

        mNavItems.add(new NavItem("Search", "Search for stops", R.drawable.ic_search_black_48dp));
        mNavItems.add(new NavItem("Home", "Keep your favourites here", R.drawable.ic_home_black_48dp));
        mNavItems.add(new NavItem("Disruptions", "Information about affected transport", R.drawable.ic_report_problem_black_48dp));
        mNavItems.add(new NavItem("Alarms", "View your transport alarms", R.drawable.ic_alarm_black_48dp));

        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        drawerList.setAdapter(adapter);

        // Drawer Item click listeners
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
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




        drawerLayout.setDrawerListener(mDrawerToggle);

        MyFragmentPagerAdapter fragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments);
        viewPager.setAdapter(fragmentPagerAdapter);

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


        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);

        fab1.setSelected(true);
        fab2.setSelected(true);
        fab3.setSelected(true);

        mMarkerArrayList = new ArrayList<>();
        ((SupportMapFragment)mFragments.get(0)).getExtendedMapAsync(this);
        buildGoogleApiClient();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mMapIsReady) {
            filterResults();
        }
        growFab();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    /**
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
        //fList.add(TramSimulatorFragment.newInstance("Fragment 3"));

        return fList;

    }

    public ArrayList<Disruption> getDisruptionsResults() {
        return mDisruptionsResults;
    }
    public ArrayList<NearMeResult> getNearMeResults() {
        return filteredResults;
    }
    public ArrayList<Values> getAlarms() {return null;}

    public Location getLocation() {
        return mLastLocation;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = ((SupportMapFragment)mFragments.get(0)).getExtendedMap();
        mGoogleMap.setOnInfoWindowClickListener(this);
        mGoogleMap.setMyLocationEnabled(true);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(-37.643099, 144.754956));
        builder.include(new LatLng(-38.434046, 145.595909));
        LatLngBounds bounds = builder.build();
        int padding = 10; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mGoogleMap.moveCamera(cu);
        mMapIsReady = true;
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
            Fragment fragment = mFragments.get(1);
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
            ((NearMeListFragment)mFragments.get(1)).setLocation(mLastLocation);
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
        this.mNearMeResults = nearMeResults;
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
                if(!mFilter.contains((String) fab.getTag())) {
                    mFilter.add((String)fab.getTag());
                }
            }
            else {
                if(mFilter.contains((String)fab.getTag())) {
                    mFilter.remove((String)fab.getTag());
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
        intent.putExtra(PTVConstants.JSON_NEARMERESULT, nearMeResult);
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
        mMarkerArrayList.clear();
        if(mGoogleMap!=null) {
            mGoogleMap.clear();
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(NearMeResult result : mNearMeResults) {
            if (mFilter.contains(result.result.transportType)) {
                filteredResults.add(result);
                LatLng loc = new LatLng(result.result.latitude, result.result.longitude);
                Marker marker= mGoogleMap.addMarker(new MarkerOptions().position(loc).title(result.result.locationName + " " + result.result.transportType).snippet(result.result.suburb).icon(ImageUtils.getTransportPinDescriptor(result.result.transportType)));
                marker.setData(result);
                builder.include(marker.getPosition());
                mMarkerArrayList.add(marker);
            }
        }
        Log.e(TAG, Integer.toString(filteredResults.size()));
        ((IResults<NearMeResult>) mFragments.get(1)).setResults((ArrayList<NearMeResult>) filteredResults.clone());
        if(mFirstLoad) {
            try {
                LatLngBounds bounds = builder.build();
                int padding = 10; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mGoogleMap.moveCamera(cu);
                mFirstLoad = false;
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
