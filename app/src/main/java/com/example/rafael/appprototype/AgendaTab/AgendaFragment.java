package com.example.rafael.appprototype.AgendaTab;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;
import java.util.List;

public class AgendaFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.content_grid_view, container, false);

        // get the patients
        List<Patient> patients = Patient.getAllPatients();
        ArrayList<Patient> patientsArrayList = new ArrayList<>();
        patientsArrayList.addAll(patients);

        // fill the GridView
        //gridView = (GridView) myInflatedView.findViewById(R.id.gridView);
        // gridView.setAdapter(new PatientsGridViewAdapter(getActivity(), patients));

        return myInflatedView;
    }
}

