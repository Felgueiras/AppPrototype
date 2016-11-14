package com.example.rafael.appprototype.DrugPrescription;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rafael.appprototype.DrugPrescription.Beers.BeersCriteria;
import com.example.rafael.appprototype.DrugPrescription.Beers.RecommendationInfo;
import com.example.rafael.appprototype.DrugPrescription.Start.PrescriptionStart;
import com.example.rafael.appprototype.DrugPrescription.Stopp.PrescriptionStopp;
import com.example.rafael.appprototype.DrugPrescription.Stopp.SelectedDrugsListAdapter;
import com.example.rafael.appprototype.DrugPrescription.Start.StartCriteria;
import com.example.rafael.appprototype.DrugPrescription.Stopp.StoppCriteria;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

/**
 * Search a drug by its name.
 */
public class SearchAllDrugs extends Fragment {

    // List view
    private ListView drugsSearchListView, selectedDrugsListView;

    // Listview Adapter
    ArrayAdapter<String> drugsListAdapter;

    // Search EditText
    EditText inputSearch;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.search_drugs, container, false);

        // list view with possible choices and selected ones
        drugsSearchListView = (ListView) v.findViewById(R.id.list_view);
        selectedDrugsListView = (ListView) v.findViewById(R.id.selectedDrugs);
        inputSearch = (EditText) v.findViewById(R.id.inputSearch);

        // stopp
        final ArrayList<String> stoppCriteriaDrugs = StoppCriteria.getAllDrugsStopp(stoppGeneral);
        // beers
        final ArrayList<String> beersCriteriaDrugs = BeersCriteria.getAllDrugsBeers(beersData);
        // start
        final ArrayList<String> startCriteriaDrugs = StartCriteria.getAllDrugsStart(startGeneral);

        // mix into a single ArrayList
        ArrayList<String> allDrugs = new ArrayList<>();
        //allDrugs.addAll(stoppCriteriaDrugs);
        //allDrugs.addAll(startCriteriaDrugs);
        allDrugs.addAll(beersCriteriaDrugs);
        // TODO check for duplicates
        drugsListAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, R.id.drug_name, allDrugs);

        drugsSearchListView.setAdapter(drugsListAdapter);
        final SelectedDrugsListAdapter displaySelectedDrugsAdapter = new SelectedDrugsListAdapter(getActivity(), selectedDrugs);
        selectedDrugsListView.setAdapter(displaySelectedDrugsAdapter);

        drugsSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDrug;
                TextView textView = (TextView) view.findViewById(R.id.drug_name);
                selectedDrug = (String) textView.getText();

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

                // reset
                inputSearch.setText("");
                drugsListAdapter.getFilter().filter("a0s0a0aa0s0");

            }
        });


        inputSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                drugsListAdapter.getFilter().filter("a0s0a0aa0s0");
            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                drugsListAdapter.getFilter().filter(cs);
                if (cs.length() == 0) {
                    drugsListAdapter.getFilter().filter("a0s0a0aa0s0");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });


        return v;
    }


}
