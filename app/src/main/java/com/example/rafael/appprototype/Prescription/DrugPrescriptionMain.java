package com.example.rafael.appprototype.Prescription;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.Prescription.Beers.BeersCriteriaFragment;
import com.example.rafael.appprototype.Prescription.Start.StartCriteriaFragment;
import com.example.rafael.appprototype.Prescription.Stopp.StoppCriteriaFragment;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.ToolbarHelper;

/**
 * Main fragment for the DrugPrescriptions; creates a ViewPager for the multiple sections (Pesquisa, Start, Stopp and Beers).
 */
public class DrugPrescriptionMain extends Fragment {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // set the title
        getActivity().setTitle(getResources().getString(R.string.tab_drug_prescription));

        //ToolbarHelper.hideBackButton(getActivity());

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.prescription_main, container, false);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());


        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) v.findViewById(R.id.container);
        viewPager.setAdapter(mSectionsPagerAdapter);
        viewPager.setCurrentItem(Constants.vpPrescriptionPage);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //system.out.println(position);
                Constants.vpPrescriptionPage = position;
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

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new SearchAllDrugs();
            } else if (position == 2) {
                return new StoppCriteriaFragment();
            } else if (position == 1) {
                return new StartCriteriaFragment();
            } else if (position == 3) {
                return new BeersCriteriaFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.drugs_all);
                case 1:
                    return getResources().getString(R.string.drugs_stopp);
                case 2:
                    return getResources().getString(R.string.drugs_start);
                case 3:
                    return getResources().getString(R.string.drugs_beers);
            }
            return null;
        }
    }
}
