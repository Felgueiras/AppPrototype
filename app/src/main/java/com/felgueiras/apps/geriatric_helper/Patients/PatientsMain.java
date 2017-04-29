package com.felgueiras.apps.geriatric_helper.Patients;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.Criteria.StartCriteria;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.BackStackHandler;
import com.felgueiras.apps.geriatric_helper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatric_helper.Patients.Favorite.PatientsFavoriteMain;
import com.felgueiras.apps.geriatric_helper.Patients.Progress.ProgressFragment;
import com.felgueiras.apps.geriatric_helper.Patients.Recent.PatientsRecent;
import com.felgueiras.apps.geriatric_helper.Patients.AllPatients.PatientsListFragment;
import com.felgueiras.apps.geriatric_helper.PatientsManagement;
import com.felgueiras.apps.geriatric_helper.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.lang.reflect.Modifier;


public class PatientsMain extends Fragment {







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_navigation_patients_sections, container, false);

        // set the title
        getActivity().setTitle(getResources().getString(R.string.tab_my_patients));




//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                //system.out.println(position);
//                Constants.vpPatientsPage = position;
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

        /**
         * Setup bottom navigation.
         */
        BottomNavigationView bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigation);


        /**
         * Default fragment.
         */
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frame_layout_bottom_navigation);
        if (currentFragment != null)
            transaction.remove(currentFragment);

        transaction.replace(R.id.frame_layout_bottom_navigation, new PatientsListFragment());
        transaction.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment = null;
                        switch (item.getItemId()) {
                            case R.id.patients_all:
                                fragment = new PatientsListFragment();
                                break;
                            case R.id.patients_favorites:
                                fragment = new PatientsFavoriteMain();
                                break;
                            case R.id.patients_recent:
                                fragment = new PatientsRecent();
                                break;
                        }

                        FragmentManager fragmentManager = getChildFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();

                        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frame_layout_bottom_navigation);
                        if (currentFragment != null)
                            transaction.remove(currentFragment);
                        transaction.replace(R.id.frame_layout_bottom_navigation, fragment);
                        transaction.commit();


                        return true;
                    }
                });

        return view;
    }
}
