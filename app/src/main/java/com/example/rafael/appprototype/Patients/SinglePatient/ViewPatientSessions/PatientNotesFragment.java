package com.example.rafael.appprototype.Patients.SinglePatient.ViewPatientSessions;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.R;


public class PatientNotesFragment extends Fragment {

    public static final String PATIENT = "PATIENT";
    private Patient patient;

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
        View view = inflater.inflate(R.layout.patient_notes, container, false);

        EditText notes = (EditText) view.findViewById(R.id.patient_notes);
        if (patient.getNotes()!=null) {
            notes.setText(patient.getNotes());
        }

        notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                patient.setNotes(charSequence.toString());
                patient.save();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }


}