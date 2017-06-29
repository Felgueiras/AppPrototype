package com.felgueiras.apps.geriatric_helper.Patients.Favorite;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.PatientsManagement;
import com.felgueiras.apps.geriatric_helper.R;

import java.util.ArrayList;


public class PatientsFavoriteFragment extends Fragment {


    private GridView gridView;
    private PatientCardFavorite adapter;

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.patients_grid, container, false);


        gridView = view.findViewById(R.id.patients_grid);
        ArrayList<PatientFirebase> favoritePatients = PatientsManagement.getInstance().getFavoritePatients(getActivity());

        adapter = new PatientCardFavorite(getActivity(), favoritePatients, this);
        gridView.setAdapter(adapter);


        return view;
    }


    /**
     * Remove a PATIENT from the favorites.
     */
    public void removePatientFromFavorites(PatientFirebase patient) {
        patient.setFavorite(false);
        // update
        PatientsManagement.getInstance().updatePatient(patient, getActivity());
//        FirebaseHelper.firebaseTablePatients.child(patient.getKey()).child("favorite").setValue(patient.isFavorite());

//        favoritePatients.remove(index);
//        recyclerView.removeViewAt(index);
//        adapter.notifyItemRemoved(index);
//        adapter.notifyItemRangeChanged(index, sessionsFromPatient.size());
        adapter.notifyDataSetChanged();

        Snackbar.make(getView(), "Paciente removido dos favoritos", Snackbar.LENGTH_SHORT).show();

    }
}