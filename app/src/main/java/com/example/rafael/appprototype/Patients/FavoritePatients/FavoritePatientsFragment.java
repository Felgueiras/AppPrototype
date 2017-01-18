package com.example.rafael.appprototype.Patients.FavoritePatients;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.EmptyStateFragment;
import com.example.rafael.appprototype.Patients.ViewPatients.CreatePatientsList;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

public class FavoritePatientsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // TODO display favorites individually with grid

        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.content_grid_favorites, container, false);
        getActivity().setTitle(getResources().getString(R.string.tab_my_patients));

        // get the patients - filter to display only favorites
        ArrayList<Patient> patients = Patient.getFavoritePatients();
        if (patients.isEmpty()) {
            /*
            FragmentManager fragmentManager = getFragmentManager();
            Fragment fragment = new EmptyStateFragment();
            Bundle args = new Bundle();
            args.putString(EmptyStateFragment.MESSAGE, getResources().getString(R.string.no_favorite_patients));
            fragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .replace(R.id.empty_state, fragment)
                    .commit();

            GridView grid = (GridView) myInflatedView.findViewById(R.id.gridView);
            grid.setVisibility(View.GONE);
            */

        } else {
            /**
             Grid view that will hold info about the Patients
             **/
            GridView gridView = (GridView) myInflatedView.findViewById(R.id.gridView);
            gridView.setAdapter(new CreatePatientsList(getActivity(), patients));
        }


        return myInflatedView;
    }
}

