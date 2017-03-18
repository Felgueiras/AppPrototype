package com.example.rafael.appprototype.Patients.Favorite;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;


public class PatientsFavoriteFragment extends Fragment {

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.patients_grid, container, false);
        // fill the GridView

        /**
         Grid view that will hold info about the Patients
         **/
        GridView gridView = (GridView) view.findViewById(R.id.patients_grid);
        ArrayList<Patient> patients = Patient.getFavoritePatients();

        gridView.setAdapter(new FavoritePatientsSingleVersion2(getActivity(), patients));

        return view;
    }
}