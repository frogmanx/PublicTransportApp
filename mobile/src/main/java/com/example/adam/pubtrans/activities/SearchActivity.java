package com.example.adam.pubtrans.activities;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.support.v7.widget.SearchView;

import com.androidmapsextensions.SupportMapFragment;
import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.SlidingTabLayout;
import com.example.adam.pubtrans.adapters.DrawerListAdapter;
import com.example.adam.pubtrans.adapters.MyFragmentPagerAdapter;
import com.example.adam.pubtrans.fragments.DisruptionsFragment;
import com.example.adam.pubtrans.fragments.NearMeListFragment;
import com.example.adam.pubtrans.fragments.TramSimulatorFragment;
import com.example.adam.pubtrans.interfaces.IPubActivity;
import com.example.adam.pubtrans.interfaces.IResults;
import com.example.adam.pubtrans.models.Disruption;
import com.example.adam.pubtrans.models.NavItem;
import com.example.adam.pubtrans.models.NearMeResult;
import com.example.adam.pubtrans.models.Values;
import com.example.adam.pubtrans.utils.WebApi;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 17/06/2015.
 */
public class SearchActivity extends BaseActivity implements IPubActivity, SearchView.OnQueryTextListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    ViewPager mViewPager;

    ArrayList<Fragment> fragments;
    ArrayList<NearMeResult> searchResults;
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        setTitle("Search");
        searchResults = new ArrayList<>();
        mViewPager = (ViewPager) findViewById(R.id.pager);
        fragments = (ArrayList<Fragment>) getFragments();

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        MyFragmentPagerAdapter fragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(fragmentPagerAdapter);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public void nearMeResponse(final ArrayList<NearMeResult> searchResults) {
        this.searchResults = searchResults;
        runOnUiThread(new Runnable()
        {

            public void run()
            {
                ((IResults) fragments.get(0)).setResults(searchResults);
            }
        });
    }

    public ArrayList<NearMeResult> getNearMeResults() {
        return searchResults;
    }


    public ArrayList<Disruption> getDisruptionsResults() {
        return null;
    }




    private List<Fragment> getFragments(){

        List<Fragment> fList = new ArrayList<>();

        fList.add(NearMeListFragment.newInstance("Fragment 1"));

        return fList;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);

        return true;
    }

    public boolean onQueryTextSubmit (String query) {
        return true;
    }

    public boolean onQueryTextChange (String newText) {
        try {
            WebApi.getSearch(newText, this);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }


}
