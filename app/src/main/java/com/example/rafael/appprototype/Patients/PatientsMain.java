package com.example.rafael.appprototype.Patients;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.Main.MainActivity;
import com.example.rafael.appprototype.Patients.ViewPatientsTab.ViewPatientsFragment;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.Sessions.NewSessionTab.Sessions;
import com.example.rafael.appprototype.Sessions.SessionsHistoryTab.SessionsHistoryFragment;
import com.example.rafael.appprototype.Tutorial.CreatePatient;


public class PatientsMain extends Fragment {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.patients_main, container, false);
        // set the title
        getActivity().setTitle(getResources().getString(R.string.tab_drug_prescription));
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) v.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // FAB
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.patients_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create a new Patient - switch to CreatePatient Fragment
                Bundle args = new Bundle();
                ((MainActivity) getActivity()).replaceFragment(CreatePatient.class, args, Constants.create_patient);
            }
        });
        return v;
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            Log.d("Drugs", "SectionsPagerAdapter");
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new ViewPatientsFragment();
            } else if (position == 1) {
                return new SessionsHistoryFragment();
            } else if (position == 2) {
                return new FavoritePatientsFragment();
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
                    return "Todos";
                case 1:
                    return "Últimos";
                case 2:
                    return "Favoritos";
            }
            return null;
        }
    }
}
