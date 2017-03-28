package com.example.rafael.appprototype.Patients.ViewPatients;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
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

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.Main.FragmentTransitions;
import com.example.rafael.appprototype.Patients.NewPatient.CreatePatient;
import com.example.rafael.appprototype.R;
import com.futuremind.recyclerviewfastscroll.FastScroller;

import java.util.ArrayList;

/**
 * Display the list of Patients to view them or select one of them.
 */
public class PatientsList extends Fragment {

    private static final String BUNDLE_RECYCLER_LAYOUT = "abc";
    public static String selectPatient = "selectPatient";
    private final ViewPager viewPager;
    private final int page;
    private PatientCardPatientsList adapter;
    private RecyclerView patientsRecyclerView;

    public PatientsList(ViewPager viewPager, int position) {
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
                Log.d("Patient", "Going to select PATIENT");
//                Constants.selectPatient = true;
            }
        }

        // fill the RecyclerView
        patientsRecyclerView = (RecyclerView) view.findViewById(R.id.patients_recycler_view);
        FastScroller fastScroller = (FastScroller) view.findViewById(R.id.fastscroll);

        // display card for each Patientndroid rec
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        patientsRecyclerView.setLayoutManager(mLayoutManager);
//        patientsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new PatientCardPatientsList(getActivity(), patients);
        patientsRecyclerView.setAdapter(adapter);

        fastScroller.setRecyclerView(patientsRecyclerView);

//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
//                layoutManager.getOrientation());
//        patientsRecyclerView.addItemDecoration(dividerItemDecoration);


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
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (Constants.patientsListBundle != null) {
            Parcelable savedRecyclerLayoutState = Constants.patientsListBundle.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            patientsRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Constants.patientsListBundle = new Bundle();
        Constants.patientsListBundle.putParcelable(BUNDLE_RECYCLER_LAYOUT, patientsRecyclerView.getLayoutManager().onSaveInstanceState());
    }
}

