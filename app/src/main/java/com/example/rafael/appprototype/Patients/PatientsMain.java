package com.example.rafael.appprototype.Patients;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.Evaluations.EvaluationsHistory.EvaluationsHistoryMain;
import com.example.rafael.appprototype.Evaluations.NewEvaluation.NewEvaluation;
import com.example.rafael.appprototype.Main.FragmentTransitions;
import com.example.rafael.appprototype.Patients.FavoritePatients.FavoritePatientsFragment;
import com.example.rafael.appprototype.Patients.NewPatient.CreatePatient;
import com.example.rafael.appprototype.Patients.ViewPatients.ViewPatientsFragment;
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
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());


        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
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

        // FAB
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.patients_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (viewPager.getCurrentItem()) {
                    case 0:
                        // create a new Patient - switch to CreatePatient Fragment
                        Bundle args = new Bundle();
                        FragmentTransitions.replaceFragment(getActivity(), new CreatePatient(), args, Constants.tag_create_patient);
                        break;
                    case 1:
                        AlertDialog chooseGender;

                        // Strings to Show In Dialog with Radio Buttons
                        final CharSequence[] items = {getString(R.string.male), getString(R.string.female)};

                        // Creating and Building the Dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(getString(R.string.select_patient_gender));
                        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                Bundle args = new Bundle();

                                switch (item) {
                                    case 0:
                                        args.putInt(NewEvaluation.GENDER, Constants.MALE);
                                        break;
                                    case 1:
                                        args.putInt(NewEvaluation.GENDER, Constants.FEMALE);
                                        break;

                                }
                                dialog.dismiss();
                                // create a new Session - switch to CreatePatient Fragment
                                FragmentTransitions.replaceFragment(getActivity(), new NewEvaluation(), args, Constants.tag_create_session);
                            }
                        });
                        chooseGender = builder.create();
                        chooseGender.show();
                        break;
                }

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
                return new EvaluationsHistoryMain();
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
