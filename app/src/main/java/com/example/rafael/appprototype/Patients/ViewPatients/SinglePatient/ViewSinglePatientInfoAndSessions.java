package com.example.rafael.appprototype.Patients.ViewPatients.SinglePatient;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.Evaluations.NewEvaluation.NewEvaluation;
import com.example.rafael.appprototype.Main.MainActivity;
import com.example.rafael.appprototype.Patients.ViewPatients.SinglePatient.ViewPatientSessions.ViewPatientSessionsFragment;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.EmptyStateFragment;
import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * Created by rafael on 05-10-2016.
 */
public class ViewSinglePatientInfoAndSessions extends Fragment {

    public static final String PATIENT = "patient";
    /**
     * Patient to be displayed
     */
    private Patient patient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO duplicated info (patient name)
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.patient_info_main, container, false);
        // get patient
        Bundle bundle = getArguments();
        patient = (Patient) bundle.getSerializable(PATIENT);
        getActivity().setTitle(patient.getName());

        // access Views
        TextView patientName = (TextView) view.findViewById(R.id.patientName);
        TextView patientAge = (TextView) view.findViewById(R.id.patientAge);
        TextView patientAddress = (TextView) view.findViewById(R.id.patientAddress);
        ImageView patientPhoto = (ImageView) view.findViewById(R.id.patientPhoto);

        // set Patient infos
        patientName.setText(patient.getName());
        patientAge.setText(patient.getAge() + "");
        patientAddress.setText(patient.getAddress());
        patientPhoto.setImageResource(patient.getPicture());

        /**
         RecyclerView to display the Patient's Records
         **/

        // get list of Records from this patient
        ArrayList<Session> sessionsFromPatient = patient.getRecordsFromPatient();
        if (sessionsFromPatient.isEmpty()) {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment fragment = new EmptyStateFragment();
            Bundle args = new Bundle();
            args.putString(EmptyStateFragment.MESSAGE, getResources().getString(R.string.no_sessions_for_patient));
            fragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .replace(R.id.patientSessionsFragment, fragment)
                    .commit();
        } else {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment fragment = new ViewPatientSessionsFragment();
            Bundle args = new Bundle();
            args.putSerializable(ViewPatientSessionsFragment.PATIENT, patient);
            fragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .replace(R.id.patientSessionsFragment, fragment)
                    .commit();
        }


        /**
         * Setup FABS
         */
        AddFloatingActionButton fabAddSession = (AddFloatingActionButton) view.findViewById(R.id.patient_createSession);
        fabAddSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putSerializable(NewEvaluation.PATIENT, patient);
                ((MainActivity) getActivity()).replaceFragment(NewEvaluation.class, args, Constants.tag_create_new_session_for_patient);
                getActivity().setTitle(getResources().getString(R.string.tab_sessions));
            }
        });
        final FloatingActionButton fabFavorite = (FloatingActionButton) view.findViewById(R.id.patient_favorite);
        if (patient.isFavorite())
            fabFavorite.setIconDrawable(getResources().getDrawable(R.drawable.ic_favorite_remove));
        else
            fabFavorite.setIconDrawable(getResources().getDrawable(R.drawable.ic_favorite_add));
        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set Patient as favorite
                patient.setFavorite(!patient.isFavorite());
                patient.save();
                if (patient.isFavorite()) {
                    Snackbar.make(getView(), R.string.patient_favorite_add, Snackbar.LENGTH_SHORT).show();
                    fabFavorite.setIconDrawable(getResources().getDrawable(R.drawable.ic_favorite_remove));
                } else {
                    Snackbar.make(getView(), R.string.patient_favorite_remove, Snackbar.LENGTH_SHORT).show();
                    fabFavorite.setIconDrawable(getResources().getDrawable(R.drawable.ic_favorite_add));
                }
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.newSession:
                Bundle args = new Bundle();
                args.putSerializable(NewEvaluation.PATIENT, patient);
                ((MainActivity) getActivity()).replaceFragment(NewEvaluation.class, args, Constants.tag_create_new_session_for_patient);
                // change the title
                getActivity().setTitle(getResources().getString(R.string.tab_sessions));
                break;
            case R.id.action_favorite:
                // set Patient as favorite
                patient.setFavorite(!patient.isFavorite());
                patient.save();
                if (patient.isFavorite())
                    Snackbar.make(getView(), R.string.patient_favorite_add, Snackbar.LENGTH_SHORT).show();
                else
                    Snackbar.make(getView(), R.string.patient_favorite_remove, Snackbar.LENGTH_SHORT).show();
        }
        return true;

    }
}

