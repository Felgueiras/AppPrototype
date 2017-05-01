package com.felgueiras.apps.geriatric_helper.Patients.Favorite;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felgueiras.apps.geriatric_helper.EmptyStateFragment;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.PatientsManagement;
import com.felgueiras.apps.geriatric_helper.R;

import java.util.ArrayList;

public class PatientsFavoriteMain extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        Log.d("Menu", "Favorite");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_grid_favorites, container, false);
        getActivity().setTitle(getResources().getString(R.string.tab_my_patients));

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        // get the patients - filter to display only favorites
        ArrayList<PatientFirebase> favoritePatients = PatientsManagement.getInstance().getFavoritePatients(getActivity());
        System.out.println("Empty? " + favoritePatients.isEmpty());

        FragmentManager fragmentManager = getChildFragmentManager();

        // update views
        if (favoritePatients.isEmpty()) {

            Fragment fragment = new EmptyStateFragment();
            Bundle args = new Bundle();
            args.putString(EmptyStateFragment.MESSAGE, getActivity().getResources().getString(R.string.no_favorite_patients));
            fragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .replace(R.id.favorite_patients_frame_layout, fragment)
                    .commit();

        } else {
            Fragment fragment = new PatientsFavoriteFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.favorite_patients_frame_layout, fragment)
                    .commit();
        }

    }

}

