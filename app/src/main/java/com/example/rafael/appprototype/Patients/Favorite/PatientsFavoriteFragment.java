package com.example.rafael.appprototype.Patients.Favorite;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;


public class PatientsFavoriteFragment extends Fragment {

    private ArrayList<Patient> favoritePatients;
    private View view;
    private BaseAdapter adapter;

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
        GridView gridView = (GridView) view.findViewById(R.id.patients_grid);
        favoritePatients = Patient.getFavoritePatients();

        adapter = new PatientCardFavorite(getActivity(), favoritePatients, this);
        gridView.setAdapter(adapter);

        return view;
    }

    /**
     * Remove a patient from the favorites.
     * @param index
     */
    public void removePatientFromFavorites(int index) {
        Patient p = favoritePatients.get(index);
        p.setFavorite(false);
        p.save();

        Snackbar.make(view,"Paciente removido dos Favoritos", Snackbar.LENGTH_SHORT).show();

        favoritePatients.remove(index);
//        recyclerView.removeViewAt(index);
//        adapter.notifyItemRemoved(index);
//        adapter.notifyItemRangeChanged(index, sessionsFromPatient.size());
        adapter.notifyDataSetChanged();
    }
}