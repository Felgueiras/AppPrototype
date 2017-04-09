package com.felgueiras.apps.geriatric_helper.Patients.Favorite;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.felgueiras.apps.geriatric_helper.Firebase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class PatientsFavoriteFragment extends Fragment {

    private ArrayList<PatientFirebase> favoritePatients = new ArrayList<>();
    private View view;
    private BaseAdapter adapter;
    private GridView gridView;
    private PatientsFavoriteFragment fragment;

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.patients_grid, container, false);
        // fill the GridView

        /**
         Grid view that will hold info about the Patients
         **/
        gridView = (GridView) view.findViewById(R.id.patients_grid);
//        favoritePatients = PatientFirebase.getFavoritePatients();

        adapter = new PatientCardFavorite(getActivity(), retrieveFavoritePatients(), this);
        gridView.setAdapter(adapter);

        fragment = this;

        return view;
    }

    private ArrayList<PatientFirebase> retrieveFavoritePatients() {
        FirebaseHelper.firebaseTablePatients.orderByChild("favorite").equalTo(true).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                favoritePatients.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    PatientFirebase patient = postSnapshot.getValue(PatientFirebase.class);
                    patient.setKey(postSnapshot.getKey());
                    favoritePatients.add(patient);
                }
                gridView.setAdapter(new PatientCardFavorite(getActivity(), favoritePatients, fragment));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        });

        return favoritePatients;
    }


    /**
     * Remove a PATIENT from the favorites.
     * @param index
     */
    public void removePatientFromFavorites(int index) {
        PatientFirebase patient = favoritePatients.get(index);
        patient.setFavorite(false);
        FirebaseHelper.firebaseTablePatients.child(patient.getKey()).child("favorite").setValue(patient.isFavorite());

        favoritePatients.remove(index);
//        recyclerView.removeViewAt(index);
//        adapter.notifyItemRemoved(index);
//        adapter.notifyItemRangeChanged(index, sessionsFromPatient.size());
        adapter.notifyDataSetChanged();
    }
}