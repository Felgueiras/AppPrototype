package com.example.rafael.appprototype.Patients;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.Evaluations.EvaluationsHistoryMain;
import com.example.rafael.appprototype.Patients.FavoritePatients.FavoritePatientsMain;
import com.example.rafael.appprototype.Patients.ViewPatients.PatientsListFragment;
import com.example.rafael.appprototype.R;


public class PatientsMain extends Fragment {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.patients_main, container, false);

        // get toolbar
        //toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // set the title
        getActivity().setTitle(getResources().getString(R.string.tab_drug_prescription));
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), viewPager);

        // Set up the ViewPager with the sections adapter.
        viewPager.setAdapter(mSectionsPagerAdapter);
        viewPager.setCurrentItem(Constants.vpPatientsPage);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //system.out.println(position);
                Constants.vpPatientsPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return v;
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final ViewPager viewPager;

        public SectionsPagerAdapter(FragmentManager fm, ViewPager viewPager) {
            super(fm);
            this.viewPager = viewPager;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new PatientsListFragment(viewPager);
            } else if (position == 1) {
                return new EvaluationsHistoryMain(viewPager);
            } else if (position == 2) {
                return new FavoritePatientsMain();
            }
            return null;
        }





        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.patients_all);
                case 1:
                    return getResources().getString(R.string.patients_sessions);
                case 2:
                    return getResources().getString(R.string.patients_favorites);
            }
            return null;
        }
    }
}
