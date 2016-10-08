package com.example.rafael.appprototype.NewSessionTab.ViewAvailableTests;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.ViewPatientsTab.SinglePatient.ViewPatientRecordsAdapter;
import com.example.rafael.appprototype.ViewPatientsTab.ViewPatientsFragment;

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
    private ViewPatientRecordsAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add_button, container, false);

        // add patient button
        ImageView addPatient = (ImageView) view.findViewById(R.id.plusButton);
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open the list of patients
                Fragment fragment = new ViewPatientsFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.patientInfo, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}