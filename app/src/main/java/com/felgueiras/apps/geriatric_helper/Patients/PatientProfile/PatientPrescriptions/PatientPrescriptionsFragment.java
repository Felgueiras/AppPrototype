package com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientPrescriptions;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PrescriptionFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.StringHelper;
import com.felgueiras.apps.geriatric_helper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatric_helper.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class PatientPrescriptionsFragment extends Fragment {

    public static final String PATIENT = "PATIENT";
    private static final String BUNDLE_RECYCLER_LAYOUT = "abc";
    private PatientFirebase patient;
    private RecyclerView recyclerView;
    private PatientPrescriptionsFragment fragment;
    private ArrayList<PrescriptionFirebase> patientsPrescriptions;
    boolean compactView = false;
    private String someVarB;


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
        View view = inflater.inflate(R.layout.patient_prescriptions, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.patientPrescriptions);

        // get switch
        Switch switchButton = (Switch) view.findViewById(R.id.compactViewSwitch);
        switchButton.bringToFront();
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    compactView = true;

                } else {
                    compactView = false;
                }

                PatientPrescriptionsCard adapter = new PatientPrescriptionsCard(
                        fragment.getActivity(),
                        patientsPrescriptions,
                        patient,
                        fragment, compactView);
                recyclerView.setAdapter(adapter);
            }
        });

        // create Layout
        int numbercolumns = 1;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), numbercolumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        fragment = this;


        /**
         * Setup FABS
         */
        FloatingActionButton fabAddPrescription = (FloatingActionButton) view.findViewById(R.id.patientAddPrescription);
        fabAddPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putSerializable(PatientPrescriptionAddMultiple.PATIENT, patient);
                FragmentTransitions.replaceFragment(getActivity(),
                        new PatientPrescriptionAddMultiple(),
                        args,
                        Constants.tag_add_prescription_to_patient);
            }
        });

        retrievePatientPrescriptions();
        return view;
    }

    /**
     * Get prescriptions from this patient.
     *
     * @return
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
                        PatientPrescriptionsCard adapter = new PatientPrescriptionsCard(
                                fragment.getActivity(),
                                patientsPrescriptions,
                                patient,
                                fragment, compactView);
                        recyclerView.setAdapter(adapter);
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