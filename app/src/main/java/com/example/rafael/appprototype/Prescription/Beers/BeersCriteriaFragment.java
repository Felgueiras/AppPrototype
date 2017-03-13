package com.example.rafael.appprototype.Prescription.Beers;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.RadioGroup;

import com.example.rafael.appprototype.DataTypes.Criteria.Beers.BeersCriteria;
import com.example.rafael.appprototype.DataTypes.Criteria.Beers.DiseaseSyndrome;
import com.example.rafael.appprototype.DataTypes.Criteria.Beers.OrganSystemWithDiseasesSyndromes;
import com.example.rafael.appprototype.DataTypes.Criteria.Beers.TherapeuticCategoryOrganSystem;
import com.example.rafael.appprototype.Prescription.Beers.ExpandableListViewAdapter.BeersCriteriaDisease;
import com.example.rafael.appprototype.Prescription.Beers.ExpandableListViewAdapter.BeersCriteriaOrganSystem;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BeersCriteriaFragment extends Fragment {




    ExpandableListView expListView;
    //OrganSystem
    List<String> listDataHeaderOrganSystem;
    HashMap<String, List<TherapeuticCategoryEntry>> listDataChildOrganSystem;
    // Disease
    List<String> listDataHeaderDisease;
    HashMap<String, List<DiseaseSyndrome>> listDataChildDisease;
    private BaseExpandableListAdapter listAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_info, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Prescrição");
                alertDialog.setMessage(getActivity().getResources().getString(R.string.info_prescription));
                alertDialog.show();
        }
        return true;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_beers_criteria, container, false);


        // get the listview
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

        // preparing list data
        prepareListDataOrganSystem();
        prepareListDataDisease();

        listAdapter = new BeersCriteriaOrganSystem(getActivity(), listDataHeaderOrganSystem, listDataChildOrganSystem);
        expListView.setAdapter(listAdapter);


        // user's selection
        RadioGroup beersSelection = (RadioGroup) view.findViewById(R.id.beersSelection);

        beersSelection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (checkedId == R.id.organSystem) {
                    listAdapter = new BeersCriteriaOrganSystem(getActivity(), listDataHeaderOrganSystem, listDataChildOrganSystem);
                    expListView.setAdapter(listAdapter);
                } else if (checkedId == R.id.disease) {
                    listAdapter = new BeersCriteriaDisease(getActivity(), listDataHeaderDisease,
                            listDataChildDisease);
                    expListView.setAdapter(listAdapter);
                }
            }
        });

        return view;
    }


    private void prepareListDataDisease() {
        listDataHeaderDisease = new ArrayList<>();
        listDataChildDisease = new HashMap<>();

        ArrayList<OrganSystemWithDiseasesSyndromes> beersOrganSystem = BeersCriteria.getBeersDiseaseSyndrome();

        // Adding child data
        for (int i = 0; i < beersOrganSystem.size(); i++) {
            OrganSystemWithDiseasesSyndromes s = beersOrganSystem.get(i);
            // header
            listDataHeaderDisease.add(s.getOrganSystem());
            // child
            List<DiseaseSyndrome> child = new ArrayList<>();
            for (DiseaseSyndrome entry : s.getDiseaseSyndromes()) {
                child.add(entry);
            }
            listDataChildDisease.put(listDataHeaderDisease.get(i), child); // Header, Child data
        }
    }

    private void prepareListDataOrganSystem() {
        listDataHeaderOrganSystem = new ArrayList<>();
        listDataChildOrganSystem = new HashMap<>();

        ArrayList<TherapeuticCategoryOrganSystem> beersTherapeuticalCategoryOrganSystem = BeersCriteria.getBeersTherapeuticCategoryOrganSystem();

        // Adding child data
        for (int i = 0; i < beersTherapeuticalCategoryOrganSystem.size(); i++) {
            TherapeuticCategoryOrganSystem s = beersTherapeuticalCategoryOrganSystem.get(i);
            // header
            listDataHeaderOrganSystem.add(s.getTherapeuticCategoryOrganSystem());
            // child
            List<TherapeuticCategoryEntry> child = new ArrayList<>();
            for (TherapeuticCategoryEntry entry : s.getEntries()) {
                child.add(entry);
            }
            listDataChildOrganSystem.put(listDataHeaderOrganSystem.get(i), child); // Header, Child data
        }
    }

}
