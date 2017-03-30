package com.example.rafael.appprototype.Patients.Progress;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;


public class ProgressMainViewPager extends Fragment {

    public static final String PATIENT = "PATIENT";
    private Patient patient;
    private TabLayout tabLayout;
    private ArrayList<Session> sessionsFromPatient;

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        patient = (Patient) bundle.getSerializable(PATIENT);
    }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_pager, container, false);

        sessionsFromPatient = patient.getSessionsFromPatient();
//        ProgressAreas adapter = new ProgressAreas(getActivity(), sessionsFromPatient, PATIENT);

        getActivity().setTitle(patient.getName() +" - " + getResources().getString(R.string.progress));

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.container);
        MyPageAdapter pageAdapter = new MyPageAdapter(getChildFragmentManager());
        setupViewPager(viewPager, pageAdapter);
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(Constants.progressPage);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Constants.progressPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }


    public void setupViewPager(ViewPager viewPager, MyPageAdapter adapter) {
        for (int i = 0; i < Constants.cga_areas.length; i++) {
            // sessions for this area
            adapter.addFragment(ProgressAreaScalesFragment.newInstance(Constants.cga_areas[i],
                    patient), getActivity().getResources().getString(R.string.drugs_beers));
        }
        viewPager.setAdapter(adapter);
    }

    private class MyPageAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> mFragmentList = new ArrayList<>();
        private ArrayList<String> mFragmentTitleList = new ArrayList<>();

        MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return this.mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Constants.cga_areas[position];
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }
}