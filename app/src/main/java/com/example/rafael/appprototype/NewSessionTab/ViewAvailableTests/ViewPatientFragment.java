package com.example.rafael.appprototype.NewSessionTab.ViewAvailableTests;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.ViewPatientsTab.SinglePatient.ViewPatientRecordsAdapter;

/**
 * Created by rafael on 06-10-2016.
 */
public class ViewPatientFragment extends Fragment {

    public static final String PATIENT = "patient";
    /**
     * Patient to be displayed
     */
    private Patient patient;
    /**
     * Adapter to the RecyclerView
     */
    private ViewPatientRecordsAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.patient_info_data, container, false);

        // get patient
        Bundle bundle = getArguments();
        patient = (Patient) bundle.getSerializable(PATIENT);
        getActivity().setTitle(patient.getName());

        // set Patient date
        TextView patientName = (TextView) view.findViewById(R.id.patientName);
        patientName.setText(patient.getName());

        // set Patient age
        //TextView patientAge = (TextView) view.findViewById(R.id.patientAge);
        //patientAge.setText(patient.getAge());

        // set Patient address
        //TextView patientAddress = (TextView) view.findViewById(R.id.patientAddress);
        //patientAddress.setText("Address");

        return view;
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getActivity().getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}