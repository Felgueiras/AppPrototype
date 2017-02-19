package com.example.rafael.appprototype.Patients.PatientProgress;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.appprototype.DataTypes.DB.GeriatricScale;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricScaleNonDB;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.R;
import com.jjoe64.graphview.GraphView;

import java.util.ArrayList;

/**
 * Created by felgueiras on 18/02/2017.
 */
public class ProgressDetail extends Fragment {
    public static final String SCALE = "SCALE";
    public static final String PATIENT = "PATIENT";
    public static final String SCALE_INFO = "SCALE_INFO";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.progress_detail, container, false);


        Bundle arguments = getArguments();
        String scale = arguments.getString(SCALE);
        Patient patient = (Patient) arguments.getSerializable(PATIENT);
        GeriatricScaleNonDB scaleInfo = (GeriatricScaleNonDB) arguments.getSerializable(SCALE_INFO);

        getActivity().setTitle(scaleInfo.getTestName());
        GraphView graphView = (GraphView) view.findViewById(R.id.graph_view);

        ArrayList<GeriatricScale> scaleInstances = GeriatricScale.getScaleInstancesForPatient(patient.getSessionsFromPatient(), scale);

        GraphViewHelper.buildGraph(graphView, scaleInstances, scaleInfo, getActivity());

        return view;
    }
}
