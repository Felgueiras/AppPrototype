package com.example.rafael.appprototype.Patients.SinglePatient.ViewPatientSessions;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;


public class PatientSessionsFragment extends Fragment {

    public static final String PATIENT = "PATIENT";
    private Patient patient;
    private ArrayList<Session> sessionsFromPatient;
    private RecyclerView recyclerView;
    private SessionCardPatientProfile adapter;

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        patient = (Patient) bundle.getSerializable(PATIENT);
    }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.patient_info_sessions, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.patientSessions);
        sessionsFromPatient = patient.getSessionsFromPatient();
        adapter = new SessionCardPatientProfile(getActivity(), sessionsFromPatient, patient, this);

        // create Layout
        int numbercolumns = 1;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), numbercolumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return view;
    }


    /**
     * Erase a session from the patient.
     *
     * @param index Session index
     */
    public void removeSession(int index) {
        sessionsFromPatient.remove(index);
        recyclerView.removeViewAt(index);
        adapter.notifyItemRemoved(index);
        adapter.notifyItemRangeChanged(index, sessionsFromPatient.size());
        adapter.notifyDataSetChanged();
    }
}