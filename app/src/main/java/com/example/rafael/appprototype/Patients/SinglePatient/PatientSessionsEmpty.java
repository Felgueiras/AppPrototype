package com.example.rafael.appprototype.Patients.SinglePatient;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.Evaluations.AllAreas.CGAPrivate;
import com.example.rafael.appprototype.HelpersHandlers.SharedPreferencesHelper;
import com.example.rafael.appprototype.Main.FragmentTransitions;
import com.example.rafael.appprototype.R;

/**
 * Created by rafael on 21-11-2016.
 */
public class PatientSessionsEmpty extends Fragment {

    public static final String MESSAGE = "MESSAGE";
    public static final String PATIENT = "PATIENT";


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_empty_state, container, false);

        Bundle bundle = getArguments();
        String messageToDisplay = bundle.getString(MESSAGE, "Empty state");
        final Patient patient = (Patient) bundle.getSerializable(PATIENT);
        TextView emptyStateTextView = (TextView) view.findViewById(R.id.emptyStateText);
        emptyStateTextView.setText(messageToDisplay);


        FloatingActionButton fabAddSession = (FloatingActionButton) view.findViewById(R.id.patient_createSession);
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
