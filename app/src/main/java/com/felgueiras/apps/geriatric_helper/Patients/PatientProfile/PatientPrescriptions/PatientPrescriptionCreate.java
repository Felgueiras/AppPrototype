package com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientPrescriptions;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PrescriptionFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.BackStackHandler;
import com.felgueiras.apps.geriatric_helper.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class PatientPrescriptionCreate extends Fragment {

    public static final String PATIENT = "patient";
    public static final String DRUG = "DRUG";
    RadioGroup radioGroup;
    private TextView name;
    private EditText notes;
    PatientFirebase patient;
    private String prescription;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null && getArguments().containsKey(PATIENT))
            patient = (PatientFirebase) getArguments().getSerializable(PATIENT);

        if (getArguments() != null && getArguments().containsKey(DRUG))
            prescription = getArguments().getString(DRUG);


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add_prescription_to_patient, container, false);
        getActivity().setTitle(getResources().getString(R.string.add_prescription));


        // get views
        name = (TextView) view.findViewById(R.id.prescriptionName);
        notes = (EditText) view.findViewById(R.id.addressText);

        if (prescription != null) {
            name.setText(prescription);
        }

        Button addPrescription = (Button) view.findViewById(R.id.saveButton);
        addPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // validate name
                if (name.getText().length() == 0) {
                    Snackbar.make(getView(), R.string.add_prescription_error_name, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // get current date
                Calendar c = Calendar.getInstance();
                Date prescriptionDate = c.getTime();

                // create new Prescription object
                PrescriptionFirebase prescription = new PrescriptionFirebase(name.getText().toString(),
                        notes.getText().toString(), prescriptionDate);
                prescription.setGuid("PRESCRIPTION" + new Random().nextInt());
                prescription.setName(name.getText().toString());
                prescription.setPatientID(patient.getGuid());
                patient.addPrescription(prescription.getGuid(), getActivity());

                // save Prescription
                FirebaseDatabaseHelper.createPrescription(prescription);

                Snackbar.make(getView(), R.string.add_prescription_success, Snackbar.LENGTH_SHORT).show();

                BackStackHandler.getFragmentManager().popBackStack();
                BackStackHandler.getFragmentManager().popBackStack();
            }
        });


        return view;

    }


}
