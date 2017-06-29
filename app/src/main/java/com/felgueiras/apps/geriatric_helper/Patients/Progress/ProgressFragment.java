package com.felgueiras.apps.geriatric_helper.Patients.Progress;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.R;

import java.util.ArrayList;


public class ProgressFragment extends Fragment {

    public static final String PATIENT = "PATIENT";
    private PatientFirebase patient;

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        patient = (PatientFirebase) bundle.getSerializable(PATIENT);
    }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_navigation_progress, container, false);

        getActivity().setTitle(patient.getName() + " - " + getResources().getString(R.string.progress));

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

        bottomNavigationView.getMenu().getItem(Constants.bottomNavigationPatientProgress).setChecked(true);
        final ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(ProgressAreaScalesFragment.newInstance(Constants.cga_mental, patient));
        fragments.add(ProgressAreaScalesFragment.newInstance(Constants.cga_functional, patient));
        fragments.add(ProgressAreaScalesFragment.newInstance(Constants.cga_nutritional, patient));
        fragments.add(ProgressAreaScalesFragment.newInstance(Constants.cga_social, patient));

        transaction.replace(R.id.frame_layout_bottom_navigation, fragments.get(Constants.bottomNavigationPatientProgress));
        transaction.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment = null;
                        switch (item.getItemId()) {
                            case R.id.cga_mental:
                                Constants.bottomNavigationPatientProgress = 0;
                                fragment = fragments.get(0);
                                break;
                            case R.id.cga_functional:
                                Constants.bottomNavigationPatientProgress = 1;
                                fragment = fragments.get(1);
                                break;
                            case R.id.cga_nutritional:
                                Constants.bottomNavigationPatientProgress = 2;
                                fragment = ProgressAreaScalesFragment.newInstance(Constants.cga_nutritional, patient);
                                break;
                            case R.id.cga_social:
                                Constants.bottomNavigationPatientProgress = 3;
                                fragment = ProgressAreaScalesFragment.newInstance(Constants.cga_social, patient);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

}