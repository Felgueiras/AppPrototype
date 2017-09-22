package com.felgueiras.apps.geriatrichelper.VerticalViewPager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.felgueiras.apps.geriatrichelper.R;

import java.util.ArrayList;
import java.util.List;

public class CustomViewPagerActivity extends AppCompatActivity {

    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view_pager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.view_pager);
        List<Fragment> fragments = getFragments();
        MyPageAdapter pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(pageAdapter);

    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<>();
        fList.add(MyFragment.newInstance("Fragment 1"));
        fList.add(MyFragment.newInstance("Fragment 2"));
        fList.add(MyFragment.newInstance("Fragment 3"));
        return fList;
    }

    class MyPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }

}
