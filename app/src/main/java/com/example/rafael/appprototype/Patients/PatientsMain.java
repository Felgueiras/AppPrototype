package com.example.rafael.appprototype.Patients;

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
import com.example.rafael.appprototype.Patients.Favorite.PatientsFavoriteMain;
import com.example.rafael.appprototype.Patients.ViewPatients.PatientsAll;
import com.example.rafael.appprototype.Patients.Recent.PatientsRecent;
import com.example.rafael.appprototype.R;


public class PatientsMain extends Fragment {


    private TabLayout tabLayout;

    /**
     * Tab icons.
     */
    private int[] tabIcons = {
            R.drawable.ic_people_black_24dp,
            R.drawable.ic_star_black_24dp,
            R.drawable.ic_access_time_black_24dp,
    };

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
        getActivity().setTitle(getResources().getString(R.string.tab_my_patients));
        ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), viewPager);

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
        setupTabIcons();

        return v;
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
//        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
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
                return new PatientsAll(viewPager, position);
            } else if (position == 1) {
                return new PatientsFavoriteMain();
            } else if (position == 2) {
                return new PatientsRecent(viewPager, position);
            }
            return null;
        }



        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return getResources().getString(R.string.patients_all);
//                case 1:
//                    return getResources().getString(R.string.patients_favorites);
//                case 2:
//                    return getResources().getString(R.string.patients_recent);
//            }
            return null;
        }
    }
}
