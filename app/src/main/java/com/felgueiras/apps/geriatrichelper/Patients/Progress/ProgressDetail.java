package com.felgueiras.apps.geriatrichelper.Patients.Progress;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felgueiras.apps.geriatrichelper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatrichelper.R;
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
        PatientFirebase patient = (PatientFirebase) arguments.getSerializable(PATIENT);
        GeriatricScaleNonDB scaleInfo = (GeriatricScaleNonDB) arguments.getSerializable(SCALE_INFO);

        getActivity().setTitle(scaleInfo.getScaleName());
        GraphView graphView = view.findViewById(R.id.graph_view);

        ArrayList<GeriatricScaleFirebase> scaleInstances =
                FirebaseDatabaseHelper.getScaleInstancesForPatient(FirebaseDatabaseHelper.getSessionsFromPatient(patient), scale);

        GraphViewHelper.buildProgressGraph(graphView, scaleInstances, scaleInfo, getActivity(), patient);

        return view;
    }
}
