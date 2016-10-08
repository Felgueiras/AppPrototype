package com.example.rafael.appprototype.NewSessionTab.DisplayTest;


import android.app.Fragment;
import android.support.v13.app.FragmentPagerAdapter;

import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricTestNonDB;
import com.example.rafael.appprototype.NewSessionTab.DisplayTest.SingleTest.DisplaySingleTestFragment;

import java.util.ArrayList;

public class TestsViewPagerAdapter extends FragmentPagerAdapter {
    /**
     * Tests for the Session
     */
    private final ArrayList<GeriatricTestNonDB> tests;


    /**
     * Constructor for the TestsViewPagerAdapter class
     *
     * @param childFragmentManager
     * @param selectedTests
     */
    public TestsViewPagerAdapter(android.app.FragmentManager childFragmentManager, ArrayList<GeriatricTestNonDB> selectedTests) {
        super(childFragmentManager);
        this.tests = selectedTests;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return tests.size();
    }

    /**
     * Retiurns the Fragment to display for that page.
     *
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        return DisplaySingleTestFragment.newInstance(position, "Page # " + (position + 1), tests.get(position));
    }

    // Returns the page question for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {

        return tests.get(position).toString();
    }

}