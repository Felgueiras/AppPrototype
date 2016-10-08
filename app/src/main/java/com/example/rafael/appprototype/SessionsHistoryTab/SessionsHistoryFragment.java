package com.example.rafael.appprototype.SessionsHistoryTab;

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
    private GridView gridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.content_grid_view, container, false);

        // get the patients
        ArrayList<Patient> patients = Patient.getAllPatients();

        // fill the GridView
        gridView = (GridView) myInflatedView.findViewById(R.id.gridView);
        gridView.setAdapter(new ShowDailyHistory(getActivity(), patients));

        return myInflatedView;
    }
}

