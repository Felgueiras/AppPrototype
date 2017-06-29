package com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientNotes;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Display notes for each of one of a patient's sessions.
 */
public class PatientNotesFragment extends Fragment {

    public static final String PATIENT = "PATIENT";
    private static final String BUNDLE_RECYCLER_LAYOUT = "abc";
    private PatientFirebase patient;
    private RecyclerView recyclerView;

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        patient = (PatientFirebase) bundle.getSerializable(PATIENT);
    }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.patient_info_notes, container, false);
        recyclerView = view.findViewById(R.id.sessionNotes);

        // create Layout
        int numbercolumns = 1;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), numbercolumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // get the patient's sessions
        retrievePatientSessions(this);

        return view;
    }


    /**
     * Retrieve the patient's sessions.
     *
     * @param fragment
     */
    private void retrievePatientSessions(final PatientNotesFragment fragment) {

        FirebaseHelper.firebaseTableSessions.orderByChild("patientID").equalTo(patient.getGuid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<SessionFirebase> patientSessions = new ArrayList<>();

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

                // include only the sessions which have associated notes
                ArrayList<SessionFirebase> sessionsWithNotes = new ArrayList<>();
                for (SessionFirebase session : patientSessions) {
                    // session has notes
                    if (session.getNotes() != null) {
                        sessionsWithNotes.add(session);
                        continue;
                    }
                    // scale inside session has notes
                    ArrayList<GeriatricScaleFirebase> scales = FirebaseDatabaseHelper.getScalesFromSession(session);
                    for (GeriatricScaleFirebase scale : scales) {
                        if (scale.getNotes() != null) {
                            sessionsWithNotes.add(session);
                            continue;
                        }

                    }

                }

                PatientNotesAdapter adapter = new PatientNotesAdapter(getActivity(),
                        sessionsWithNotes,
                        patient);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        });
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        Bundle bundle = Constants.patientsSessionsBundle.get(patient.getGuid());
        if (bundle != null) {
            Parcelable savedRecyclerLayoutState = bundle.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Bundle bundle = new Bundle();
        Constants.patientsSessionsBundle.put(patient.getGuid(), bundle);
        Constants.patientsSessionsBundle.get(patient.getGuid()).putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
    }
}