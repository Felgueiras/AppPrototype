package com.felgueiras.apps.geriatric_helper.Prescription;

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

import com.felgueiras.apps.geriatric_helper.Prescription.AllDrugs.PrescriptionAllDrugs;
import com.felgueiras.apps.geriatric_helper.Prescription.Beers.BeersCriteriaFragment;
import com.felgueiras.apps.geriatric_helper.Prescription.Start.PrescriptionStartCriteriaFragment;
import com.felgueiras.apps.geriatric_helper.Prescription.Stopp.PrescriptionStoppCriteriaFragment;
import com.felgueiras.apps.geriatric_helper.R;

/**
 * Main fragment for the DrugPrescriptions; creates a ViewPager for the multiple sections (Pesquisa, Start, Stopp and Beers).
 */
public class PrescriptionMainFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_navigation_prescription, container, false);

        // set the title
        getActivity().setTitle(getResources().getString(R.string.tab_drug_prescription));


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

        transaction.replace(R.id.frame_layout_bottom_navigation, new PrescriptionAllDrugs());
        transaction.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment = null;
                        switch (item.getItemId()) {
                            case R.id.drugs_all:
                                fragment = new PrescriptionAllDrugs();
                                break;
                            case R.id.drugs_start:
                                fragment = new PrescriptionStartCriteriaFragment();
                                break;
                            case R.id.drugs_stopp:
                                fragment = new PrescriptionStoppCriteriaFragment();
                                break;
                            case R.id.drugs_beers:
                                fragment = new BeersCriteriaFragment();
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
