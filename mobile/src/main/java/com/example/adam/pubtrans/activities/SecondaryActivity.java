package com.example.adam.pubtrans.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.adam.pubtrans.fragments.BroadNextDepaturesListFragment;
import com.example.adam.pubtrans.interfaces.Callback;
import com.example.adam.pubtrans.interfaces.IFabAnimate;
import com.example.adam.pubtrans.utils.PTVConstants;
import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.interfaces.IResults;
import com.example.adam.pubtrans.models.BroadNextDeparturesResult;
import com.example.adam.pubtrans.models.NearMeResult;
import com.example.adam.pubtrans.utils.SharedPreferencesHelper;
import com.example.adam.pubtrans.utils.WebApi;
import com.example.adam.pubtrans.views.SelectableFloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Adam on 31/05/2015.
 */
public class SecondaryActivity extends BaseActivity implements Callback<ArrayList<BroadNextDeparturesResult>>, View.OnClickListener, IFabAnimate {
    public static final String ARG_DRAWING_START_LOCATION = "arg_drawing_start_location";
    public static final int MY_SNACKBAR_LENGTH = 3000;
    FragmentManager fragmentManager;
    private int drawingStartLocation;
    @Bind(R.id.contentRoot) LinearLayout contentRoot;
    NearMeResult nearMeResult;
    @Bind(R.id.favouriteFab) SelectableFloatingActionButton favouriteFab;
    @Bind(R.id.my_awesome_toolbar) Toolbar toolbar;
    @Bind(R.id.favourite_card_view) CardView cardView;

    int cx;
    int cy;
    float radius;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondary_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        setTitle("Broad Next Departures");
        nearMeResult = getIntent().getParcelableExtra(PTVConstants.JSON_NEARMERESULT);


        favouriteFab.setOnClickListener(this);
        if(SharedPreferencesHelper.isFavouriteStop(this, nearMeResult)) {
            favouriteFab.setImageResource(R.drawable.star);
        }
        else {
            favouriteFab.setImageResource(R.drawable.star_outline);
        }

        try {
            WebApi.getBroadNextDepatures(nearMeResult.result.transportType, nearMeResult.result.stopId,5,this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentManager = getSupportFragmentManager();

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

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        cardView.setCardBackgroundColor(getResources().getColor(R.color.secondary));

    }

    public NearMeResult getSelectedStop() {
        return nearMeResult;
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
                        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment);
                        ((BroadNextDepaturesListFragment) fragment).animateHeader();
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

    public void shrinkCardView() {
        if(cardView.getVisibility()==View.VISIBLE) {
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

    public void acknowledge(View v) {
        hideBottomBar();
    }

    public void hideBottomBar() {
        shrinkCardView();
        if(favouriteFab.getVisibility()==View.INVISIBLE) {
            growFab();
        }
    }

    @Override
    public void onBackPressed() {
        if(cardView.getVisibility()==View.VISIBLE) {
            shrinkCardView();
            growFab();
        }
        else {
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

        if(id == android.R.id.home){
            // app icon in action bar clicked; goto parent activity.
            onBackPressed();
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
                    revealView(v);
                }
                else {
                    removeFavouriteCurrentStop();
                    favouriteFab.setImageResource(R.drawable.star_outline);
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

    public void success(final ArrayList<BroadNextDeparturesResult> broadNextDeparturesResults) {
        runOnUiThread(new Runnable() {
            public void run() {
                Fragment fragment = fragmentManager.findFragmentById(R.id.fragment);
                if(fragment!=null) {
                    ((IResults) fragment).setResults(broadNextDeparturesResults);
                }
            }
        });
    }

    public void revealView(View view) {
        final View myView = view;
        cx = (view.getLeft() + view.getRight()) / 2;
        cy = (view.getTop() + view.getBottom()) / 2;
        Log.e("Reveal", Integer.toString(cx));
        Log.e("Reveal", Integer.toString(view.getLeft()));
        Log.e("Reveal", Integer.toString(view.getRight()));
        Log.e("Reveal", Integer.toString(cy));
        Log.e("Reveal", Integer.toString(view.getTop()));
        Log.e("Reveal", Integer.toString(view.getBottom()));

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

        cardView.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideBottomBar();
            }
        }, MY_SNACKBAR_LENGTH);

    }
}
