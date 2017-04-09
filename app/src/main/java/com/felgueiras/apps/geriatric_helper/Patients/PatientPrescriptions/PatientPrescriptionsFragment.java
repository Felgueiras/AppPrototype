package com.felgueiras.apps.geriatric_helper.Patients.PatientPrescriptions;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.Evaluations.AllAreas.CGAPrivate;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.PrescriptionFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.SharedPreferencesHelper;
import com.felgueiras.apps.geriatric_helper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatric_helper.Patients.NewPatient.CreatePatient;
import com.felgueiras.apps.geriatric_helper.R;

import java.util.ArrayList;


public class PatientPrescriptionsFragment extends Fragment {

    public static final String PATIENT = "PATIENT";
    private static final String BUNDLE_RECYCLER_LAYOUT = "abc";
    private PatientFirebase patient;
    private RecyclerView recyclerView;
    private PatientPrescriptionsCard adapter;
    private ArrayList<PrescriptionFirebase> prescriptionsFromPatient;

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
        prescriptionsFromPatient = FirebaseHelper.getPrescriptionsFromPatient(patient);
        adapter = new PatientPrescriptionsCard(getActivity(), prescriptionsFromPatient, patient, this);

        // create Layout
        int numbercolumns = 1;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), numbercolumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        /**
         * Setup FABS
         */
        FloatingActionButton fabAddPrescription = (FloatingActionButton) view.findViewById(R.id.patientAddPrescription);
        fabAddPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putSerializable(CreatePrescription.PATIENT, patient);
                FragmentTransitions.replaceFragment(getActivity(),
                        new CreatePrescription(),
                        args,
                        Constants.tag_add_prescription_to_patient);
            }
        });

        return view;
    }


    /**
     * Erase a session from the PATIENT.
     *
     * @param index Session index
     */
    public void removePrescription(int index) {
        prescriptionsFromPatient.remove(index);
        recyclerView.removeViewAt(index);
        adapter.notifyItemRemoved(index);
        adapter.notifyItemRangeChanged(index, prescriptionsFromPatient.size());
        adapter.notifyDataSetChanged();
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