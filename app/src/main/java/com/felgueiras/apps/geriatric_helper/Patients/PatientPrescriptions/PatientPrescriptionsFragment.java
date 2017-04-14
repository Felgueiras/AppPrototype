package com.felgueiras.apps.geriatric_helper.Patients.PatientPrescriptions;

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

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.PrescriptionFirebase;
import com.felgueiras.apps.geriatric_helper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatric_helper.Prescription.AllDrugs.PrescriptionAllDrugs;
import com.felgueiras.apps.geriatric_helper.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class PatientPrescriptionsFragment extends Fragment {

    public static final String PATIENT = "PATIENT";
    private static final String BUNDLE_RECYCLER_LAYOUT = "abc";
    private PatientFirebase patient;
    private RecyclerView recyclerView;
    private PatientPrescriptionsFragment fragment;
    private ArrayList<PrescriptionFirebase> patientsPrescriptions = new ArrayList<>();


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
        patientsPrescriptions = FirebaseHelper.getPrescriptionsFromPatient(patient);

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
                args.putSerializable(PickPrescription.PATIENT, patient);
                FragmentTransitions.replaceFragment(getActivity(),
                        new PickPrescription(),
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
        FirebaseHelper.firebaseTablePrescriptions.orderByChild("patientID").equalTo(patient.getGuid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                patientsPrescriptions.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    PrescriptionFirebase prescription = postSnapshot.getValue(PrescriptionFirebase.class);
                    prescription.setKey(postSnapshot.getKey());
                    patientsPrescriptions.add(prescription);
                }
                Log.d("Firebase", "Retrieved patients prescriptions.");
                PatientPrescriptionsCard adapter = new PatientPrescriptionsCard(
                        fragment.getActivity(),
                        patientsPrescriptions,
                        patient,
                        fragment);
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
        FirebaseHelper.deletePrescription(prescription);
//        recyclerView.removeViewAt(index);
        // TODo
//        adapter.notifyItemRemoved(index);
//        adapter.notifyItemRangeChanged(index, prescriptionsFromPatient.size());
//        adapter.notifyDataSetChanged();
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