package com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientPrescriptions;

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
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PrescriptionFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.StringHelper;
import com.felgueiras.apps.geriatric_helper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientPrescriptions.AddPrescriptions.PatientPrescriptionsAddFragment;
import com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientTimeline.Orientation;
import com.felgueiras.apps.geriatric_helper.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class PatientPrescriptionsTimelineFragment extends Fragment {

    public static final String PATIENT = "PATIENT";
    private PatientFirebase patient;
    private ArrayList<PrescriptionFirebase> patientsPrescriptions;


    private RecyclerView mRecyclerView;
    private PrescriptionsDailyAdapter mTimeLineAdapter;
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private boolean compactView = true;
    private PatientPrescriptionsTimelineFragment fragment;


    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        patient = (PatientFirebase) bundle.getSerializable(PATIENT);

        mOrientation = Orientation.VERTICAL;
        mWithLinePadding = false;

        fragment = this;
    }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prescriptions_timeline, container, false);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);


        /**
         * Setup FABS
         */
        FloatingActionButton fabAddPrescription = (FloatingActionButton) view.findViewById(R.id.patientAddPrescription);
        fabAddPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putSerializable(PatientPrescriptionsAddFragment.PATIENT, patient);
                FragmentTransitions.replaceFragment(getActivity(),
                        new PatientPrescriptionsAddFragment(),
                        args,
                        Constants.tag_add_prescription_to_patient);
            }
        });

//        Switch switchButton = (Switch) view.findViewById(R.id.compactViewSwitch);
//        switchButton.bringToFront();
//        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
//                if (bChecked) {
//                    compactView = true;
//
//                } else {
//                    compactView = false;
//                }
//
//                mTimeLineAdapter = new PrescriptionsDailyAdapter(patientsPrescriptions,
//                        mOrientation,
//                        mWithLinePadding,
//                        getActivity(),
//                        compactView);
//                mRecyclerView.setAdapter(mTimeLineAdapter);
//            }
//        });

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
        retrievePatientPrescriptions();
    }


    /**
     * Retrieve the patient's sessions.
     */
    private void retrievePatientPrescriptions() {

        FirebaseHelper.firebaseTablePrescriptions.orderByChild("patientID")
                .equalTo(patient.getGuid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        patientsPrescriptions = new ArrayList<>();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            PrescriptionFirebase prescription = postSnapshot.getValue(PrescriptionFirebase.class);
                            prescription.setKey(postSnapshot.getKey());
                            patientsPrescriptions.add(prescription);
                        }
                        // sort by name
                        Collections.sort(patientsPrescriptions, new Comparator<PrescriptionFirebase>() {
                            public int compare(PrescriptionFirebase o1, PrescriptionFirebase o2) {
                                String first = StringHelper.removeAccents(o1.getName());
                                String second = StringHelper.removeAccents(o2.getName());
                                return first.compareTo(second);
                            }
                        });

                        mTimeLineAdapter = new PrescriptionsDailyAdapter(patientsPrescriptions,
                                mOrientation, mWithLinePadding, getActivity(), compactView, fragment);
                        mRecyclerView.setAdapter(mTimeLineAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }


                });


    }

    /**
     * Erase a session from the PATIENT.
     *
     * @param prescription Session index
     */
    public void removePrescription(PrescriptionFirebase prescription) {
        FirebaseDatabaseHelper.deletePrescription(prescription, getActivity());
        retrievePatientPrescriptions();
    }


}