package com.felgueiras.apps.geriatrichelper.Sessions;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felgueiras.apps.geriatrichelper.Constants;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatrichelper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatrichelper.Patients.NewPatient.CreatePatientFragment;
import com.felgueiras.apps.geriatrichelper.PatientsManagement;
import com.felgueiras.apps.geriatrichelper.R;

import java.util.ArrayList;

/**
 * Display the list of Patients to view them or select one of them.
 */
public class PickPatientFragment extends Fragment {

    public static final String PICK_BEFORE_SESSION = "PICK_BEFORE_SESSION";
    private PatientCardPicker adapter;
    private boolean pickBeforeSession;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);


        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.listsearch).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.pick_patient, container, false);
        getActivity().setTitle(getResources().getString(R.string.pick_patient));

        // get the patients
        ArrayList<PatientFirebase> patients = PatientsManagement.getInstance().getPatients(getActivity());

        // fill the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);


        pickBeforeSession = false;
        if (getArguments() != null && getArguments().containsKey(PICK_BEFORE_SESSION))
            pickBeforeSession = getArguments().getBoolean(PICK_BEFORE_SESSION, false);

        // create Layout
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new PatientCardPicker(getActivity(), patients, pickBeforeSession);
        recyclerView.setAdapter(adapter);


        // FAB
        final FloatingActionButton fab = view.findViewById(R.id.patients_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // create a new Patient - switch to CreatePatientFragment Fragment
                Bundle bundle = new Bundle();
                if(pickBeforeSession)
                {
                    // create new patient before session start
                    bundle.putInt(CreatePatientFragment.CREATE_PATIENT_TYPE, CreatePatientFragment.CREATE_BEFORE_SESSION);
                    FragmentTransitions.replaceFragment(getActivity(),
                            new CreatePatientFragment(),
                            bundle,
                            Constants.tag_create_patient_session_start);
                }
                else
                {
                    // create new patient after session finish
                    bundle.putInt(CreatePatientFragment.CREATE_PATIENT_TYPE, CreatePatientFragment.CREATE_AFTER_SESSION);
                    FragmentTransitions.replaceFragment(getActivity(),
                            new CreatePatientFragment(),
                            bundle,
                            Constants.tag_create_patient_session_start);
                }

            }
        });

        return view;
    }
}

