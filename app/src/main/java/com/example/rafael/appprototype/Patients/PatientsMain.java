package com.example.rafael.appprototype.Patients;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.appprototype.DrugPrescription.Beers.BeersCriteriaFragment;
import com.example.rafael.appprototype.DrugPrescription.SearchAllDrugs;
import com.example.rafael.appprototype.DrugPrescription.Start.StartCriteriaFragment;
import com.example.rafael.appprototype.DrugPrescription.Stopp.StoppCriteriaFragment;
import com.example.rafael.appprototype.Patients.ViewPatientsTab.ViewPatientsFragment;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.Sessions.SessionsHistoryTab.SessionsHistoryFragment;


public class PatientsMain extends Fragment {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // set the title
        getActivity().setTitle(getResources().getString(R.string.tab_drug_prescription));

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_tabs2, container, false);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) v.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
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
