package com.example.rafael.appprototype.Patients.Favorite;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.EmptyStateFragment;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

public class PatientsFavoriteMain extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        Log.d("Menu","Favorite");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // TODO display favorites individually with grid
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_grid_favorites, container, false);
        getActivity().setTitle(getResources().getString(R.string.tab_my_patients));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        // get the patients - filter to display only favorites
        ArrayList<Patient> patients = Patient.getFavoritePatients();
        System.out.println("Empty? " + patients.isEmpty());
        FragmentManager fragmentManager = getChildFragmentManager();

        if (patients.isEmpty()) {

            Fragment fragment = new EmptyStateFragment();
            Bundle args = new Bundle();
            args.putString(EmptyStateFragment.MESSAGE, getResources().getString(R.string.no_favorite_patients));
            fragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .replace(R.id.favorite_patients_frame_layout, fragment)
                    .commit();


        } else {
            Fragment fragment = new PatientsFavoriteFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.favorite_patients_frame_layout, fragment)
                    .commit();
        }

    }
}

