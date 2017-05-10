package com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientSessions;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.SharedPreferencesHelper;
import com.felgueiras.apps.geriatric_helper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientTimeline.Orientation;
import com.felgueiras.apps.geriatric_helper.R;
import com.felgueiras.apps.geriatric_helper.Sessions.AllAreas.CGAPrivate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class PatientSessionsTimelineFragment extends Fragment {

    public static final String PATIENT = "PATIENT";
    private PatientFirebase patient;
    ArrayList<SessionFirebase> patientSessions;


    private RecyclerView mRecyclerView;
    private TimeLineAdapterSessions mTimeLineAdapter;
    private Orientation mOrientation;
    private boolean mWithLinePadding;


    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        patient = (PatientFirebase) bundle.getSerializable(PATIENT);

        mOrientation = Orientation.VERTICAL;
        mWithLinePadding = false;
    }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.patient_timeline, container, false);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);

        /**
         * Setup FABS
         */
        FloatingActionButton fabAddSession = (FloatingActionButton) view.findViewById(R.id.patient_createSession);
        fabAddSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putSerializable(CGAPrivate.PATIENT, patient);
                // pass the previous session
                SharedPreferencesHelper.unlockSessionCreation(getActivity());
                FragmentTransitions.replaceFragment(getActivity(), new CGAPrivate(), args, Constants.tag_create_session_with_patient);
                getActivity().setTitle(getResources().getString(R.string.cga));
            }
        });

        // create timeline
        initView();
        return view;
    }

    private LinearLayoutManager getLinearLayoutManager() {
        if (mOrientation == Orientation.HORIZONTAL) {
            return new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        } else {
            return new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        }
    }

    /**
     * Create timeline.
     */
    private void initView() {
        retrievePatientSessions();
    }



    /**
     * Retrieve the patient's sessions.
     *
     */
    private void retrievePatientSessions() {

        FirebaseHelper.firebaseTableSessions.orderByChild("patientID").equalTo(patient.getGuid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                patientSessions = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    SessionFirebase sessions = postSnapshot.getValue(SessionFirebase.class);
                    sessions.setKey(postSnapshot.getKey());
                    patientSessions.add(sessions);
                }

                // sort by date descending
                Collections.sort(patientSessions, new Comparator<SessionFirebase>() {
                    public int compare(SessionFirebase o1, SessionFirebase o2) {
                        return new Date(o2.getDate()).compareTo(new Date(o1.getDate()));
                    }
                });


                mTimeLineAdapter = new TimeLineAdapterSessions(patientSessions, mOrientation, mWithLinePadding, getActivity());
                mRecyclerView.setAdapter(mTimeLineAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        });
    }


}