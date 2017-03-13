package com.example.rafael.appprototype.Patients.ViewPatients;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.Main.FragmentTransitions;
import com.example.rafael.appprototype.Main.PrivateArea;
import com.example.rafael.appprototype.Patients.NewPatient.CreatePatient;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

/**
 * Display the list of Patients to view them or select one of them.
 */
public class PatientsAll extends Fragment {

    public static String selectPatient = "selectPatient";
    private final ViewPager viewPager;
    private final int page;
    private PatientCard adapter;
    private RecyclerView patientsRecyclerView;

    public PatientsAll(ViewPager viewPager, int position) {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_patients_list, container, false);

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

        // fill the RecyclerView
        patientsRecyclerView = (RecyclerView) view.findViewById(R.id.patients_recycler_view);

        // display card for each Patientndroid rec
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        patientsRecyclerView.setLayoutManager(mLayoutManager);
        patientsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new PatientCard(getActivity(), patients);
        patientsRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                layoutManager.getOrientation());
        patientsRecyclerView.addItemDecoration(dividerItemDecoration);


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

        // hide/show FAB when scrolling
        patientsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fab.isShown()) {
                    fab.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        // save the patients recylcer view state
        RecyclerView.LayoutManager layoutManager = patientsRecyclerView.getLayoutManager();
        if (layoutManager != null && layoutManager instanceof LinearLayoutManager) {
            int position = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            if (position != -1) {
                Constants.patientsListPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                Log.d("Patients", "Storing position " + Constants.patientsListPosition);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        RecyclerView.LayoutManager layoutManager = patientsRecyclerView.getLayoutManager();
        int count = layoutManager.getChildCount();
        Log.d("Patients", "Restoring position " + Constants.patientsListPosition);
        if (Constants.patientsListPosition != RecyclerView.NO_POSITION && Constants.patientsListPosition < count) {
            layoutManager.scrollToPosition(Constants.patientsListPosition);
        }
    }
}
