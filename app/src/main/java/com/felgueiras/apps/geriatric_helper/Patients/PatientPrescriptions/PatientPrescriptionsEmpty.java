package com.felgueiras.apps.geriatric_helper.Patients.PatientPrescriptions;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.SharedPreferencesHelper;
import com.felgueiras.apps.geriatric_helper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatric_helper.R;
import com.felgueiras.apps.geriatric_helper.Sessions.AllAreas.CGAPrivate;

/**
 * Created by rafael on 21-11-2016.
 */
public class PatientPrescriptionsEmpty extends Fragment {

    public static final String MESSAGE = "MESSAGE";
    public static final String PATIENT = "PATIENT";


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_empty_state_fab, container, false);

        Bundle bundle = getArguments();
        String messageToDisplay = bundle.getString(MESSAGE, "Empty state");
        final PatientFirebase patient = (PatientFirebase) bundle.getSerializable(PATIENT);
        TextView emptyStateTextView = (TextView) view.findViewById(R.id.emptyStateText);
        emptyStateTextView.setText(messageToDisplay);


        FloatingActionButton fabAddSession = (FloatingActionButton) view.findViewById(R.id.patient_createSession);
        fabAddSession.setOnClickListener(new View.OnClickListener() {
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


        return view;
    }


}
