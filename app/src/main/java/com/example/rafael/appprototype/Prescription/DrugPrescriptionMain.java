package com.example.rafael.appprototype.Prescription;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.Prescription.Beers.BeersCriteriaFragment;
import com.example.rafael.appprototype.Prescription.Start.StartCriteriaFragment;
import com.example.rafael.appprototype.Prescription.Stopp.StoppCriteriaFragment;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Main fragment for the DrugPrescriptions; creates a ViewPager for the multiple sections (Pesquisa, Start, Stopp and Beers).
 */
public class DrugPrescriptionMain extends Fragment {


    private TabLayout tabLayout;
    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // set the title
        getActivity().setTitle(getResources().getString(R.string.tab_drug_prescription));

        View v = inflater.inflate(R.layout.prescription_main2, container, false);
//        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ToolbarHelper.hideBackButton(getActivity());

        // Inflate the layout for this fragment
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());


        // Set up the ViewPager with the sections adapter.
        ViewPager viewPager = (ViewPager) v.findViewById(R.id.container);
        setupViewPager(viewPager, mSectionsPagerAdapter);
        viewPager.setCurrentItem(Constants.vpPrescriptionPage);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Constants.vpPrescriptionPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        return v;
    }

    private void setupTabIcons() {

        tabLayout.getTabAt(0).setIcon(R.drawable.pill_black);

        TextView startTab = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.tab_start, null);
        startTab.setText(getResources().getString(R.string.drugs_start).toUpperCase());
//        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_favourite, 0, 0);
        tabLayout.getTabAt(1).setCustomView(startTab);

        TextView stoppTab = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.tab_stopp, null);
        stoppTab.setText(getResources().getString(R.string.drugs_stopp).toUpperCase());
//        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_call, 0, 0);
        tabLayout.getTabAt(2).setCustomView(stoppTab);

        TextView beersTab = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.tab_beers, null);
        beersTab.setText(getResources().getString(R.string.drugs_beers).toUpperCase());
//        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_contacts, 0, 0);
        tabLayout.getTabAt(3).setCustomView(beersTab);
    }

    public void setupViewPager(ViewPager viewPager, SectionsPagerAdapter adapter) {
        adapter.addFragment(new DrugsAll(), null);
        adapter.addFragment(new StartCriteriaFragment(), getResources().getString(R.string.drugs_start));
        adapter.addFragment(new StoppCriteriaFragment(), getResources().getString(R.string.drugs_stopp));
        adapter.addFragment(new BeersCriteriaFragment(), getResources().getString(R.string.drugs_beers));
        viewPager.setAdapter(adapter);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
