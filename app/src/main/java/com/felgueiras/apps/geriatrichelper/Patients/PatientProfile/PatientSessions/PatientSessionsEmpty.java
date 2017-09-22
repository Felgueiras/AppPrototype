package com.felgueiras.apps.geriatrichelper.Patients.PatientProfile.PatientSessions;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.felgueiras.apps.geriatrichelper.Constants;
import com.felgueiras.apps.geriatrichelper.Sessions.AllAreas.CGAPrivate;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatrichelper.HelpersHandlers.SharedPreferencesHelper;
import com.felgueiras.apps.geriatrichelper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatrichelper.R;

/**
 * Created by rafael on 21-11-2016.
 */
public class PatientSessionsEmpty extends Fragment {

    public static final String MESSAGE = "MESSAGE";
    public static final String PATIENT = "PATIENT";


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_empty_state_fab, container, false);

        Bundle bundle = getArguments();
        String messageToDisplay = bundle.getString(MESSAGE, "Empty state");
        final PatientFirebase patient = (PatientFirebase) bundle.getSerializable(PATIENT);
        TextView emptyStateTextView = view.findViewById(R.id.emptyStateText);
        emptyStateTextView.setText(messageToDisplay);


        FloatingActionButton fabAddSession = view.findViewById(R.id.patient_createSession);
        fabAddSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putSerializable(CGAPrivate.PATIENT, patient);
                SharedPreferencesHelper.unlockSessionCreation(getActivity());
                FragmentTransitions.replaceFragment(getActivity(), new CGAPrivate(), args, Constants.tag_create_session_with_patient);
                getActivity().setTitle(getResources().getString(R.string.cga));
            }
        });


        return view;
    }


}
