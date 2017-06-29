package com.felgueiras.apps.geriatric_helper.Patients;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.Patients.Favorite.PatientsFavoriteMain;
import com.felgueiras.apps.geriatric_helper.Patients.Recent.PatientsRecent;
import com.felgueiras.apps.geriatric_helper.Patients.AllPatients.PatientsListFragment;
import com.felgueiras.apps.geriatric_helper.R;

import java.util.ArrayList;


public class PatientsMain extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_navigation_patients_sections, container, false);

        // set the title
        getActivity().setTitle(getResources().getString(R.string.tab_my_patients));

        /**
         * Setup bottom navigation.
         */
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);


        /**
         * Default fragment.
         */
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frame_layout_bottom_navigation);
        if (currentFragment != null)
            transaction.remove(currentFragment);

        // put fragments in array
        final ArrayList<Fragment> fragments = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Fragment frag = null;
            Bundle args = null;
            switch (i) {
                case 0:
                    frag = new PatientsListFragment();
                    break;
                case 1:
                    frag = new PatientsFavoriteMain();
                    break;
                case 2:
                    frag = new PatientsRecent();
                    break;

            }
            fragments.add(frag);
        }



        transaction.replace(R.id.frame_layout_bottom_navigation, fragments.get(Constants.vpPatientsPage));
        transaction.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment = null;
                        switch (item.getItemId()) {
                            case R.id.patients_all:
                                if (Constants.vpPatientsPage == 0)
                                    return true;
                                Constants.vpPatientsPage = 0;
                                break;
                            case R.id.patients_favorites:
                                if (Constants.vpPatientsPage == 1)
                                    return true;
                                Constants.vpPatientsPage = 1;
                                break;
                            case R.id.patients_recent:
                                if (Constants.vpPatientsPage == 2)
                                    return true;
                                Constants.vpPatientsPage = 2;
                                break;
                        }



                        fragment = fragments.get(Constants.vpPatientsPage);

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
