package com.example.rafael.appprototype.Evaluations.NewEvaluation;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.Main.MainActivity;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.Patients.ViewPatients.SinglePatient.ViewPatientSessions.CreatePatientSessionCard;
import com.example.rafael.appprototype.Patients.ViewPatients.ViewPatientsFragment;

/**
 * Created by rafael on 06-10-2016.
 */
public class SelectPatientFragment extends Fragment {

    public static final String PATIENT = "patient";
    /**
     * Patient to be displayed
     */
    private Patient patient;
    /**
     * Adapter to the RecyclerView
     */
    private CreatePatientSessionCard adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.select_patient, container, false);

        // add patient button
        TextView addPatient = (TextView) view.findViewById(R.id.selectPatient);
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open the list of patients
                Bundle args = new Bundle();
                args.putBoolean(ViewPatientsFragment.selectPatient, true);
                ((MainActivity) getActivity()).replaceFragment(ViewPatientsFragment.class, args, Constants.fragment_show_patients);
            }
        });

        return view;
    }
}