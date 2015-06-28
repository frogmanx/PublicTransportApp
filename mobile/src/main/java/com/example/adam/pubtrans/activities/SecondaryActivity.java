package com.example.adam.pubtrans.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.example.adam.pubtrans.interfaces.IFabAnimate;
import com.example.adam.pubtrans.models.Disruption;
import com.example.adam.pubtrans.models.DisruptionsResult;
import com.example.adam.pubtrans.models.Stop;
import com.example.adam.pubtrans.models.Values;
import com.example.adam.pubtrans.utils.PTVConstants;
import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.interfaces.IResults;
import com.example.adam.pubtrans.interfaces.IWebApiResponse;
import com.example.adam.pubtrans.models.BroadNextDeparturesResult;
import com.example.adam.pubtrans.models.NearMeResult;
import com.example.adam.pubtrans.utils.SharedPreferencesHelper;
import com.example.adam.pubtrans.utils.WebApi;
import com.example.adam.pubtrans.views.SelectableFloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Adam on 31/05/2015.
 */
public class SecondaryActivity extends BaseActivity implements IWebApiResponse, View.OnClickListener, IFabAnimate {
    public static final String ARG_DRAWING_START_LOCATION = "arg_drawing_start_location";
    FragmentManager fragmentManager;
    private int drawingStartLocation;
    LinearLayout contentRoot;
    NearMeResult nearMeResult;
    SelectableFloatingActionButton favouriteFab;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondary_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        setTitle("Broad Next Departures");
        String jsonNearMeResult = getIntent().getStringExtra(PTVConstants.JSON_NEARMERESULT);
        Gson gson = new Gson();

        nearMeResult =gson.fromJson(jsonNearMeResult, NearMeResult.class);

        favouriteFab = (SelectableFloatingActionButton) findViewById(R.id.favouriteFab);
        favouriteFab.setOnClickListener(this);
        if(SharedPreferencesHelper.isFavouriteStop(this, nearMeResult)) {
            favouriteFab.setImageResource(R.drawable.star);
            //favouriteFab.setColorNormal(getResources().getColor(R.color.secondary));
            //favouriteFab.setColorPressed(getResources().getColor(R.color.secondaryFallback1));
            //favouriteFab.setColorRipple(getResources().getColor(R.color.secondaryFallback2));
        }
        else {
            favouriteFab.setImageResource(R.drawable.star_outline);
            //favouriteFab.setColorNormal(getResources().getColor(R.color.primary));
            //favouriteFab.setColorPressed(getResources().getColor(R.color.primaryDark));
            //favouriteFab.setColorRipple(getResources().getColor(R.color.primaryLight));
        }

        try {
            WebApi.getBroadNextDepatures(nearMeResult.transportType, nearMeResult.stopId,5,this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentManager = getSupportFragmentManager();

        contentRoot = (LinearLayout) findViewById(R.id.contentRoot);

        drawingStartLocation = getIntent().getIntExtra(ARG_DRAWING_START_LOCATION, 0);
        if (savedInstanceState == null) {
            contentRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    contentRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                    startIntroAnimation();
                    return true;
                }
            });
        }

    }

    private void startIntroAnimation() {
        growFab();
        contentRoot.setScaleY(0.1f);
        contentRoot.setPivotY(drawingStartLocation);
        contentRoot.animate()
                .scaleY(1)
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //animateContent();
                    }
                })
                .start();
    }

    public void shrinkFab() {
        Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_shrink);
        favouriteFab.startAnimation(animScale);
    }

    public void growFab() {
        Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_grow);
        favouriteFab.startAnimation(animScale);
    }

    /*private void animateContent() {
        commentsAdapter.updateItems();
        llAddComment.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(200)
                .start();
    }*/

    @Override
    public void onBackPressed() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        shrinkFab();
        contentRoot.animate()
                .translationY(displaymetrics.heightPixels)
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        SecondaryActivity.super.onBackPressed();
                        overridePendingTransition(0, 0);
                    }
                })
                .start();
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
            Fragment fragment = fragmentManager.findFragmentById(R.id.fragment);
            if(fragment!=null) {
                ((IResults) fragment).refresh();
            }
            Bundle bundle = getIntent().getExtras();
            try {
                WebApi.getBroadNextDepatures(bundle.getString(PTVConstants.TRANSPORT_TYPE), bundle.getInt(PTVConstants.STOP_ID),0,this);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.favouriteFab: {
                if(!isFavouriteStop()) {
                    setFavouriteCurrentStop();
                    favouriteFab.setImageResource(R.drawable.star);
                    //favouriteFab.setColorNormal(getResources().getColor(R.color.secondary));
                    //favouriteFab.setColorPressed(getResources().getColor(R.color.secondaryFallback1));
                    //favouriteFab.setColorRipple(getResources().getColor(R.color.secondaryFallback2));
                }
                else {
                    removeFavouriteCurrentStop();
                    favouriteFab.setImageResource(R.drawable.star_outline);
                    //favouriteFab.setColorNormal(getResources().getColor(R.color.primary));
                    //favouriteFab.setColorPressed(getResources().getColor(R.color.primaryDark));
                    //favouriteFab.setColorRipple(getResources().getColor(R.color.primaryLight));
                }

                break;
            }

            default:
                break;
        }
    }

    public boolean isFavouriteStop() {
        return SharedPreferencesHelper.isFavouriteStop(this, nearMeResult);
    }

    public void setFavouriteCurrentStop() {
        SharedPreferencesHelper.saveFavouriteStop(this, nearMeResult);
    }

    public void removeFavouriteCurrentStop() {
        SharedPreferencesHelper.removeFavouriteStop(this, nearMeResult);
    }

    @Override
    public void broadNextDeparturesResponse(final ArrayList<BroadNextDeparturesResult> broadNextDeparturesResults) {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                Fragment fragment = fragmentManager.findFragmentById(R.id.fragment);
                if(fragment!=null) {
                    ((IResults) fragment).setResults(broadNextDeparturesResults);
                }
            }
        });


    }



}
