package com.example.adam.pubtrans.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private final String[] TITLES = {"Map", "List", "Test"};
    private ArrayList<Fragment> mFragments;

    public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentArrayList) {
        super(fm);
        this.mFragments = fragmentArrayList;
    }

    @Override
    public String getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}