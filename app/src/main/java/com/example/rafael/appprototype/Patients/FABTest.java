package com.example.rafael.appprototype.Patients;

import android.app.Fragment;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.Patients.ViewPatientsTab.ViewPatientsList;
import com.example.rafael.appprototype.R;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;

import java.util.ArrayList;

/**
 * Display the list of Patients to view them or select one of them.
 */
public class FABTest extends Fragment {

    public static String selectPatient = "selectPatient";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.fab_test, container, false);
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

        final FABToolbarLayout layout = (FABToolbarLayout) myInflatedView.findViewById(R.id.fabtoolbar);

        FloatingActionButton fab = (FloatingActionButton) myInflatedView.findViewById(R.id.fabtoolbar_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.show();

            }
        });

        ImageView one = (ImageView) myInflatedView.findViewById(R.id.one);
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("1");
            }
        });


        /**
         Grid view that will hold info about the Patients
         **/
        //GridView gridView = (GridView) myInflatedView.findViewById(R.id.gridView);
        //gridView.setAdapter(new ViewPatientsList(getActivity(), patients));

        return myInflatedView;
    }
}

