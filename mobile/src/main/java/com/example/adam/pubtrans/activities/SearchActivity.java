package com.example.adam.pubtrans.activities;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.support.v7.widget.SearchView;

import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.adapters.MyFragmentPagerAdapter;
import com.example.adam.pubtrans.fragments.NearMeListFragment;
import com.example.adam.pubtrans.interfaces.Callback;
import com.example.adam.pubtrans.interfaces.IPubActivity;
import com.example.adam.pubtrans.interfaces.IResults;
import com.example.adam.pubtrans.models.Disruption;
import com.example.adam.pubtrans.models.NearMeResult;
import com.example.adam.pubtrans.models.Values;
import com.example.adam.pubtrans.utils.WebApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Adam on 17/06/2015.
 */
public class SearchActivity extends BaseActivity implements Callback<ArrayList<NearMeResult>>,IPubActivity, SearchView.OnQueryTextListener{


    @Bind(R.id.pager) ViewPager mViewPager;
    @Bind(R.id.my_awesome_toolbar) Toolbar toolbar;
    ArrayList<Fragment> mFragments;
    ArrayList<NearMeResult> mSearchResults;
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        ButterKnife.bind(this);
        setTitle("Search");
        mSearchResults = new ArrayList<>();
        mFragments = (ArrayList<Fragment>) getFragments();

        setSupportActionBar(toolbar);

        MyFragmentPagerAdapter fragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(fragmentPagerAdapter);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


    }

    public void success(ArrayList<NearMeResult> searchNearMeResults) {
        this.mSearchResults = searchNearMeResults;
        runOnUiThread(new Runnable()
        {

            public void run()
            {
                ((IResults) mFragments.get(0)).setResults(mSearchResults);
            }
        });
    }


    public ArrayList<NearMeResult> getNearMeResults() {
        return mSearchResults;
    }


    public ArrayList<Disruption> getDisruptionsResults() {
        return null;
    }
    public ArrayList<Values> getAlarms() {
        return null;
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
        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setTextColor(Color.WHITE);
        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(Color.WHITE);
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
