package com.example.rafael.appprototype.Evaluations.SessionsHistoryTab;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

public class SessionsHistoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.content_view_patients, container, false);

        // get the patients
        ArrayList<Patient> patients = Patient.getAllPatients();

        // fill the GridView
        GridView gridView = (GridView) myInflatedView.findViewById(R.id.gridView);
        gridView.setAdapter(new ShowEvaluationsForDay(getActivity()));

        return myInflatedView;
    }
}

