package com.example.adam.pubtrans.fragments;

import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.adam.pubtrans.R;
import com.example.adam.pubtrans.activities.MainActivity;
import com.example.adam.pubtrans.adapters.DisruptionsAdapter;
import com.example.adam.pubtrans.interfaces.IResults;
import com.example.adam.pubtrans.models.Disruption;
import com.github.clans.fab.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Adam on 14/06/2015.
 */
public class TramSimulatorFragment extends Fragment implements View.OnClickListener{

    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    MediaPlayer mPlayer;
    @Bind(R.id.fab1) FloatingActionButton fab1;

    public static final TramSimulatorFragment newInstance(String message) {
        TramSimulatorFragment f = new TramSimulatorFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);
        return f;
    }

    public TramSimulatorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tram_sim_fragment, container, false);
        ButterKnife.bind(this, v);

        fab1.setOnClickListener(this);

        try {
            AssetFileDescriptor afd = getActivity().getAssets().openFd("ding.mp3");
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mPlayer.prepare();
        }catch (IOException e) {
            e.printStackTrace();
        }

        return v;
    }

    public void onClick(View v){
        try {
            AssetFileDescriptor afd = getActivity().getAssets().openFd("ding.mp3");
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mPlayer.prepare();
        }catch (IOException e) {
            e.printStackTrace();
        }

        mPlayer.start();

    }

}