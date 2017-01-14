package com.example.rafael.appprototype.DrugPrescription;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rafael.appprototype.DrugPrescription.Beers.BeersCriteria;
import com.example.rafael.appprototype.DrugPrescription.Start.StartCriteria;
import com.example.rafael.appprototype.DrugPrescription.Stopp.StoppCriteria;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

/**
 * Search a drug by its name.
 */
public class SearchAllDrugs extends Fragment {

    // List view
    private ListView drugsSearchListView;

    // Listview Adapter
    ArrayAdapter<String> adapter;

    /**
     * Selected drugs.
     */
    ArrayList<PrescriptionGeneral> selectedDrugs = new ArrayList<>();
    /**
     * Stopp criteria.
     */
    private ArrayList<StoppCriteria> stoppGeneral;
    /**
     * Beers criteria.
     */
    private ArrayList<BeersCriteria> beersData;
    /**
     * Start criteria.
     */
    private ArrayList<StartCriteria> startGeneral;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get all the criterias
        beersData = BeersCriteria.getBeersData();
        stoppGeneral = StoppCriteria.getStoppData();
        startGeneral = StartCriteria.getStartData();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.search_drugs, container, false);

        // list view with possible choices and selected ones
        drugsSearchListView = (ListView) v.findViewById(R.id.list_view);

        // stopp
        final ArrayList<String> stoppCriteriaDrugs = StoppCriteria.getAllDrugsStopp(stoppGeneral);
        // beers
        final ArrayList<String> beersCriteriaDrugs = BeersCriteria.getAllDrugsBeers(beersData);
        // start
        final ArrayList<String> startCriteriaDrugs = StartCriteria.getAllDrugsStart(startGeneral);

        // mix into a single ArrayList
        ArrayList<String> allDrugs = new ArrayList<>();
        allDrugs.addAll(stoppCriteriaDrugs);
        allDrugs.addAll(startCriteriaDrugs);
        allDrugs.addAll(beersCriteriaDrugs);
        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, R.id.drug_name, allDrugs);

        drugsSearchListView.setAdapter(adapter);

        drugsSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDrug;
                TextView textView = (TextView) view.findViewById(R.id.drug_name);
                selectedDrug = (String) textView.getText();

                // TODO open page showing infos about that drug
                /*
                // check info (start, stopp or beers)
                if (stoppCriteriaDrugs.contains(selectedDrug)) {
                    // get info about the Stopp criteria for that drug
                    PrescriptionStopp pr = StoppCriteria.getStoppCriteriaPresciptionForDrug(selectedDrug, stoppGeneral);
                    selectedDrugs.add(pr);
                    displaySelectedDrugsAdapter.notifyDataSetChanged();
                } else if (startCriteriaDrugs.contains(selectedDrug)) {
                    // get info about the Start criteria for that drug
                    PrescriptionStart pr = StartCriteria.getStartCriteriaPresciptionForDrug(selectedDrug, startGeneral);
                    selectedDrugs.add(pr);
                    displaySelectedDrugsAdapter.notifyDataSetChanged();
                } else if (beersCriteriaDrugs.contains(selectedDrug)) {
                    RecommendationInfo drugInfo = BeersCriteria.getBeersCriteriaInfoAboutDrug(selectedDrug, beersData);
                    drugInfo.setDrugName(selectedDrug);
                    selectedDrugs.add(drugInfo);
                }
                */
            }
        });


        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

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
