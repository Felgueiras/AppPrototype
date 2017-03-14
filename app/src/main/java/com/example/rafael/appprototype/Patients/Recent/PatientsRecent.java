package com.example.rafael.appprototype.Patients.Recent;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.Main.FragmentTransitions;
import com.example.rafael.appprototype.Patients.NewPatient.CreatePatient;
import com.example.rafael.appprototype.Patients.Recent.PatientsByDayAdapter;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

/**
 * Display the list of Patients to view them or select one of them.
 */
public class PatientsRecent extends Fragment {

    public static String selectPatient = "selectPatient";
    private final ViewPager viewPager;
    private final int page;
    private PatientsByDayAdapter adapter;

    public PatientsRecent(ViewPager viewPager, int position) {
        this.viewPager = viewPager;
        this.page = position;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (viewPager.getCurrentItem() == page) {
            menu.clear();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_patients_recent, container, false);

        // get the patients
        ArrayList<Patient> patients = Patient.getAllPatients();

        // read arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.getBoolean(selectPatient, false)) {
                Log.d("Patient", "Going to select patient");
//                Constants.selectPatient = true;
            }
        }

        // fill the GridView
        GridView gridView = (GridView) view.findViewById(R.id.patients_grid_view);

        adapter = new PatientsByDayAdapter(getActivity(), this);
        gridView.setAdapter(adapter);


        // FAB
        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.patients_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // create a new Patient - switch to CreatePatient Fragment
                Bundle args = new Bundle();
                FragmentTransitions.replaceFragment(getActivity(), new CreatePatient(), args, Constants.tag_create_patient);
            }
        });

//        // hide/show FAB when scrolling
//        patientsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if (dy > 0 || dy < 0 && fab.isShown()) {
//                    fab.hide();
//                }
//            }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    fab.show();
//                }
//
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//        });

        return view;
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        // save the patients recylcer view state
//        RecyclerView.LayoutManager layoutManager = patientsRecyclerView.getLayoutManager();
//        if (layoutManager != null && layoutManager instanceof LinearLayoutManager) {
//            int position = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
//            if (position != -1) {
//                Constants.patientsListPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
//                Log.d("Patients", "Storing position " + Constants.patientsListPosition);
//            }
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        RecyclerView.LayoutManager layoutManager = patientsRecyclerView.getLayoutManager();
//        int count = layoutManager.getChildCount();
//        Log.d("Patients", "Restoring position " + Constants.patientsListPosition);
//        if (Constants.patientsListPosition != RecyclerView.NO_POSITION && Constants.patientsListPosition < count) {
//            layoutManager.scrollToPosition(Constants.patientsListPosition);
//        }
//    }
}

