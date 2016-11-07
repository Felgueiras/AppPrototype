package com.example.rafael.appprototype.DrugPrescription.BeersCriteria;

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

import com.example.rafael.appprototype.DrugPrescription.StartStopp.Prescription;
import com.example.rafael.appprototype.DrugPrescription.StartStopp.StoppCriterion;
import com.example.rafael.appprototype.DrugPrescription.StartStopp.StoppGeneral;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

public class BeersCriteriaFragment extends Fragment {

    // List view
    private ListView drugsSearchList, selectedDrugsList;

    // Listview Adapter
    ArrayAdapter<String> drugsListAdapter;

    // Search EditText
    EditText inputSearch;

    BeersRecommendation beersData;

    ArrayList<RecommendationInfo> selectedBeers = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addBeersData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_search, container, false);

        // list view with possible choices and selected ones
        drugsSearchList = (ListView) v.findViewById(R.id.list_view);
        selectedDrugsList = (ListView) v.findViewById(R.id.selectedDrugs);
        inputSearch = (EditText) v.findViewById(R.id.inputSearch);

        final ArrayList<String> beersCriteriaDrugs = new ArrayList<>();
        beersCriteriaDrugs.add("Brompheniramine");
        beersCriteriaDrugs.add("Carbinoxamine");
        beersCriteriaDrugs.add("Chlorpheniramine");
        beersCriteriaDrugs.add("Clemastine");
        beersCriteriaDrugs.add("Cyproheptadine");


        final String testType = "beers";
        if (testType.equals("beers")) {
            // Beers criteria
            drugsListAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, R.id.drug_name, beersCriteriaDrugs);
        }

        drugsSearchList.setAdapter(drugsListAdapter);
        // TODO remove
        //selectedBeers.add(stoppCriteriaDrugs.get(1));
        BeersCriteriaAdapter customAdapter = new BeersCriteriaAdapter(getActivity(), selectedBeers, beersData);
        selectedDrugsList.setAdapter(customAdapter);
        /**
         * Item was selected from list.
         */
        drugsSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDrug;

                if (testType.equals("beers")) {
                    TextView textView = (TextView) view.findViewById(R.id.drug_name);
                    selectedDrug = (String) textView.getText();
                    RecommendationInfo drugInfo = getBeersCriteriaInfoAboutDrug(selectedDrug);
                    if (drugInfo != null) {

                    }
                    selectedBeers.add(drugInfo);
                }
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


    /**
     * Return the Beers criteria beersData for a certain drug name.
     *
     * @param selectedDrug
     * @return
     */
    public RecommendationInfo getBeersCriteriaInfoAboutDrug(String selectedDrug) {
        ArrayList<TherapeuticCategoryEntry> entries = beersData.getEntries();
        for (TherapeuticCategoryEntry entry : entries) {
            ArrayList<String> drugs = entry.getDrugs();
            if (drugs.contains(selectedDrug)) {
                return entry.getInfo();
            }
        }
        return null;
    }

    /**
     * Add Stopp and Beers data.
     */
    private void addBeersData() {

        /**
         * Beers criteria.
         */
        beersData = new BeersRecommendation("Anticholinergics (excludes TCAs)");
        TherapeuticCategoryEntry entry = new TherapeuticCategoryEntry("First-generation" +
                " antihistamines (as single agent or as part of combination products)");
        RecommendationInfo recommendationInfo = new RecommendationInfo("Avoid", "Highly anticholinergic; clearance reduced with advanced age, and\n" +
                "tolerance develops when used as hypnotic; increased risk of confu-\n" +
                "sion, dry mouth, constipation, and other anticholinergic effects/\n" +
                "toxicity.");
        recommendationInfo.setQualityOfEvidence("High (Hydroxyzine and Promethazine), Moderate (All others)");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        ArrayList<String> drugs = new ArrayList<>();
        drugs.add("Brompheniramine");
        drugs.add("Carbinoxamine");
        drugs.add("Chlorpheniramine");
        entry.setDrugs(drugs);

        beersData.addEntry(entry);
    }

    /**
     * Get all the drugs from StoppGeneral.
     *
     * @return
     */
    public ArrayList<String> getAllDrugsStopp(StoppGeneral general) {
        ArrayList<String> drugs = new ArrayList<>();
        ArrayList<StoppCriterion> criterions = general.getCriterions();
        for (StoppCriterion cr : criterions) {
            ArrayList<Prescription> prescriptions = cr.getPrescriptions();
            for (Prescription pr : prescriptions) {
                drugs.add(pr.getDrugName());
            }
        }
        return drugs;
    }
}
