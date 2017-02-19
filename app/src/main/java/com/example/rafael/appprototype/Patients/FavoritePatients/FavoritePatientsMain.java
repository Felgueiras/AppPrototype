package com.example.rafael.appprototype.Patients.FavoritePatients;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.EmptyStateFragment;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

public class FavoritePatientsMain extends Fragment {

    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // TODO display favorites individually with grid
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_grid_favorites, container, false);
        getActivity().setTitle(getResources().getString(R.string.tab_my_patients));

        // get the patients - filter to display only favorites
        ArrayList<Patient> patients = Patient.getFavoritePatients();
        System.out.println("Empty? " + patients.isEmpty());
        fragmentManager = getFragmentManager();

        if (patients.isEmpty()) {

            // TODO review why it crashes
//            Fragment fragment = new EmptyStateFragment();
//            Bundle args = new Bundle();
//            args.putString(EmptyStateFragment.MESSAGE, getResources().getString(R.string.no_favorite_patients));
//            fragment.setArguments(args);
//            fragmentManager.beginTransaction()
//                    .replace(R.id.favorite_patients_frame_layout, fragment)
//                    .commit();


        } else {
            Fragment fragment = new FavoritePatientsFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.favorite_patients_frame_layout, fragment)
                    .commit();
        }

        return view;
    }
}

