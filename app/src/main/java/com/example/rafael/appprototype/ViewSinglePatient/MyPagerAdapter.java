package com.example.rafael.appprototype.ViewSinglePatient;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.rafael.appprototype.DataTypes.DB.Session;

import java.util.ArrayList;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private final ArrayList<Session> sessions;
    // private static int NUM_ITEMS = 3;

    public MyPagerAdapter(FragmentManager fragmentManager, ArrayList<Session> sessions) {
        //this.sessions = sessions;
        super(fragmentManager);
        this.sessions = sessions;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return sessions.size();
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        return DisplayRecordFragment.newInstance(position, "Page # "+(position+1), sessions.get(position));
    }

    // Returns the page date for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {

        return sessions.get(position).toString();
    }

}