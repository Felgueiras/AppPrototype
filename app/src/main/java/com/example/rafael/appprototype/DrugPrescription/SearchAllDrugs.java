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

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.Criteria.BeersCriteria;
import com.example.rafael.appprototype.DataTypes.Criteria.PrescriptionGeneral;
import com.example.rafael.appprototype.DataTypes.Criteria.StartCriteria;
import com.example.rafael.appprototype.DataTypes.Criteria.StoppCriteria;
import com.example.rafael.appprototype.Main.MainActivity;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Search a drug by its name.
 */
public class SearchAllDrugs extends Fragment {

    // List view
    private ListView drugsList;

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
        drugsList = (ListView) v.findViewById(R.id.list_view);

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
        // sort list
        Collections.sort(allDrugs);

        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, R.id.drug_name, allDrugs);

        drugsList.setAdapter(adapter);

        drugsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDrug;
                TextView textView = (TextView) view.findViewById(R.id.drug_name);
                selectedDrug = (String) textView.getText();

                Fragment endFragment = new ViewSingleDrugtInfo();
                Bundle args = new Bundle();
                args.putString(ViewSingleDrugtInfo.DRUG, selectedDrug);
                ((MainActivity) getActivity()).replaceFragment(endFragment, args, Constants.tag_view_drug_info);

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
