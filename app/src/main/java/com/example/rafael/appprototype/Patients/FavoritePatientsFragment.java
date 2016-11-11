package com.example.rafael.appprototype.Patients;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.Patients.ViewPatientsTab.PatientsGridViewAdapter;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

public class FavoritePatientsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.content_grid_view, container, false);
        getActivity().setTitle(getResources().getString(R.string.tab_my_patients));

        // get the patients - filter to display only favorites
        ArrayList<Patient> patients = Patient.getFavoritePatients();

        /**
         Grid view that will hold info about the Patients
         **/
        GridView gridView = (GridView) myInflatedView.findViewById(R.id.gridView);
        gridView.setAdapter(new PatientsGridViewAdapter(getActivity(), patients));

        return myInflatedView;
    }
}

