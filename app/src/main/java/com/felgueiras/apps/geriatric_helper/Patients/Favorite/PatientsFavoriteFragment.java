package com.felgueiras.apps.geriatric_helper.Patients.Favorite;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class PatientsFavoriteFragment extends Fragment {


    private GridView gridView;

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

        /**
         Grid view that will hold info about the Patients
         **/
        gridView = (GridView) view.findViewById(R.id.patients_grid);

        retrieveFavoritePatients(this);

        return view;
    }

    private void retrieveFavoritePatients(final PatientsFavoriteFragment fragment) {
//        FirebaseHelper.firebaseTablePatients.orderByChild("favorite").equalTo(true).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                ArrayList<PatientFirebase> favoritePatients = new ArrayList<>();
//
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    PatientFirebase patient = postSnapshot.getValue(PatientFirebase.class);
//                    patient.setKey(postSnapshot.getKey());
//                    favoritePatients.add(patient);
//                }
//                Log.d("Firebase", "Retrieved favorite patients");
//                gridView.setAdapter(new PatientCardFavorite(getActivity(), favoritePatients, fragment));
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//            }
//        });
    }


    /**
     * Remove a PATIENT from the favorites.
     *
//     * @param index
     */
    public void removePatientFromFavorites(PatientFirebase patient) {
        patient.setFavorite(false);
//        FirebaseHelper.firebaseTablePatients.child(patient.getKey()).child("favorite").setValue(patient.isFavorite());

//        favoritePatients.remove(index);
//        recyclerView.removeViewAt(index);
//        adapter.notifyItemRemoved(index);
//        adapter.notifyItemRangeChanged(index, sessionsFromPatient.size());
//        adapter.notifyDataSetChanged();
    }
}