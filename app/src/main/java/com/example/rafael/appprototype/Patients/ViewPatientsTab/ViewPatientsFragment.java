package com.example.rafael.appprototype.Patients.ViewPatientsTab;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

/**
 * Display the list of Patients to view them or select one of them.
 */
public class ViewPatientsFragment extends Fragment {

    public static String selectPatient = "selectPatient";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.content_grid_view, container, false);
        getActivity().setTitle(getResources().getString(R.string.tab_my_patients));

        // get the patients
        ArrayList<Patient> patients = Patient.getAllPatients();

        // read arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.getBoolean(selectPatient, false)) {
                Log.d("Patient", "Going to select patient");
                Constants.selectPatient = true;
            }
        }

        /**
         Grid view that will hold info about the Patients
         **/
        GridView gridView = (GridView) myInflatedView.findViewById(R.id.gridView);
        gridView.setAdapter(new ViewPatientsList(getActivity(), patients));

        return myInflatedView;
    }
}

