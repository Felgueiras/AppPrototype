package com.example.rafael.appprototype.ViewPatientsTab;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPatientsFragment extends Fragment {

    /**
     * Grid view that will hold info about the Patients
     */
    private GridView gridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.content_grid_view, container, false);

        // get the patients
        ArrayList<Patient> patients = Patient.getAllPatients();

        Log.d("Patients","Viewing patients");

        // fill the GridView
        gridView = (GridView) myInflatedView.findViewById(R.id.gridView);
        gridView.setAdapter(new PatientsGridViewAdapter(getActivity(), patients));

        return myInflatedView;
    }
}

