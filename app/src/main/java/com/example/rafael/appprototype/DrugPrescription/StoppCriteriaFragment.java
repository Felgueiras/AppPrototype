package com.example.rafael.appprototype.DrugPrescription;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.rafael.appprototype.DrugPrescription.BeersCriteria.BeersRecommendation;
import com.example.rafael.appprototype.DrugPrescription.BeersCriteria.RecommendationInfo;
import com.example.rafael.appprototype.DrugPrescription.BeersCriteria.TherapeuticCategoryEntry;
import com.example.rafael.appprototype.DrugPrescription.StartStopp.Issue;
import com.example.rafael.appprototype.DrugPrescription.StartStopp.Prescription;
import com.example.rafael.appprototype.DrugPrescription.StartStopp.PrescriptionStart;
import com.example.rafael.appprototype.DrugPrescription.StartStopp.StartCriterion;
import com.example.rafael.appprototype.DrugPrescription.StartStopp.StoppCriterion;
import com.example.rafael.appprototype.DrugPrescription.StartStopp.StoppGeneral;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

public class StoppCriteriaFragment extends Fragment {

    // List view
    private ListView drugsSearchList, selectedDrugsList;

    // Listview Adapter
    ArrayAdapter<String> drugsListAdapter, selectedDrugsAdapter;

    // Search EditText
    EditText inputSearch;

    private BeersRecommendation recommendation;

    ArrayList<String> selectedDrugsArray = new ArrayList<>();
    private StoppGeneral stoppGeneral;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // add start, stopp and beers criteria
        addStartStoppBeersData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_search, container, false);

        /**
         * Set the items on the GUI
         */
        //selectedDrugInfo = (TextView) findViewById(R.id.selectedDrugInfo);

        // list view with possible choices and selected ones
        drugsSearchList = (ListView) v.findViewById(R.id.list_view);
        selectedDrugsList = (ListView) v.findViewById(R.id.selectedDrugs);
        inputSearch = (EditText) v.findViewById(R.id.inputSearch);

        /**
         * Test the Stopp criteria
         */
        final ArrayList<String> stoppCriteriaDrugs = new ArrayList<>();
        stoppCriteriaDrugs.add("Non-steroidal anti-inflammatory drug (NSAID)");
        stoppCriteriaDrugs.add("Bladder antimuscarinic drugs");
        stoppCriteriaDrugs.add("Alpha-blockers");
        stoppCriteriaDrugs.add("Diphenoxylate, loperamide or codeine phosphate");
        stoppCriteriaDrugs.add("Prochlorperazine or metoclopramide");
        stoppCriteriaDrugs.add("Proton pump inhibitor at treatment dose");
        stoppCriteriaDrugs.add("Anticholinergic antispasmodic drugs");
        stoppCriteriaDrugs.add("Glibenclamide or chlorpropamide");
        stoppCriteriaDrugs.add("Beta-blockers");
        stoppCriteriaDrugs.add("Oestrogens");
        stoppCriteriaDrugs.add("Digoxin");

        /**
         * Test the Beers criteria.
         */
        final ArrayList<String> beersCriteriaDrugs = new ArrayList<>();
        beersCriteriaDrugs.add("Brompheniramine");
        beersCriteriaDrugs.add("Carbinoxamine");
        beersCriteriaDrugs.add("Chlorpheniramine");
        beersCriteriaDrugs.add("Clemastine");
        beersCriteriaDrugs.add("Cyproheptadine");


        final String testType = "stopp";
        if (testType.equals("stopp")) {
            // stopp Criteria
            drugsListAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, R.id.drug_name, stoppCriteriaDrugs);
        } else if (testType.equals("beers")) {
            // Beers criteria
            drugsListAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, R.id.drug_name, beersCriteriaDrugs);
        }

        drugsSearchList.setAdapter(drugsListAdapter);
        selectedDrugsArray.add(stoppCriteriaDrugs.get(1));
        selectedDrugsAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, R.id.drug_name, selectedDrugsArray);

        SelectedDrugsListAdapter customAdapter = new SelectedDrugsListAdapter(getActivity(), selectedDrugsArray, stoppGeneral);
        selectedDrugsList.setAdapter(customAdapter);

        drugsSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDrug;

                if (testType.equals("stopp")) {
                    selectedDrug = stoppCriteriaDrugs.get(position);
                    // get info about the Stopp criteria for that drug
                    //selectedDrugInfo.setText("");
                    Prescription pr = stoppGeneral.getStoppCriteriaPresciptionForDrug(selectedDrug);
                    //selectedDrugsAdapter.notifyDataSetChanged();
                    if (pr != null) {
                        ArrayList<Issue> situations = pr.getIssues();
                        String drugInfo = "";
                        for (Issue issue : situations) {
                            Log.d("Search", String.valueOf(issue));
                            drugInfo += issue.toString() + "\n";
                        }
                        //selectedDrugInfo.setText(selectedDrug);
                        //selectedDrugInfo.append(drugInfo);

                    }
                    // add to selected list
                    Log.d("Drugs", "Adding to selected list");
                    selectedDrugsArray.add(selectedDrug);
                    selectedDrugsAdapter.notifyDataSetChanged();
                } else if (testType.equals("beers")) {
                    selectedDrug = beersCriteriaDrugs.get(position);
                    RecommendationInfo drugInfo = getBeersCriteriaInfoAboutDrug(selectedDrug);
                    if (drugInfo != null) {
                        //selectedDrugInfo.setText(drugInfo.toString());
                    }
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
     * Return the Beers criteria recommendation for a certain drug name.
     *
     * @param selectedDrug
     * @return
     */
    public RecommendationInfo getBeersCriteriaInfoAboutDrug(String selectedDrug) {
        ArrayList<TherapeuticCategoryEntry> entries = recommendation.getEntries();
        for (TherapeuticCategoryEntry entry : entries) {
            ArrayList<String> drugs = entry.getDrugs();
            if (drugs.contains(selectedDrug)) {
                return entry.getInfo();
            }
        }
        return null;
    }

    private void addStartStoppBeersData() {
        // add StoppCriterion
        StoppCriterion urogenitalSystem = new StoppCriterion("Urogenital System");
        Prescription pr = new Prescription("Bladder antimuscarinic drugs");
        Issue issue = new Issue("with dementia", "risk of increased confusion, agitation");
        // add Issue to Prescription
        pr.addIssue(issue);
        pr.addIssue(new Issue("with chronic glaucoma", "risk of acute exacerbation of glaucoma"));
        // add Prescription to StoppCriterion
        urogenitalSystem.addPrescription(pr);
        stoppGeneral = new StoppGeneral();
        stoppGeneral.addStoppCriterion(urogenitalSystem);


        // add StartCriterions
        StartCriterion endocryneSystem = new StartCriterion("Endocrine System");
        PrescriptionStart prescriptionStart = new PrescriptionStart("Metformin", "Metformin with type 2 diabetes +/- metabolic syndrome" +
                "(in the absence of renal impairment—estimated GFR <50ml/ min).");
        endocryneSystem.addPrescription(prescriptionStart);

        // add BeersCriteria
        recommendation = new BeersRecommendation("Anticholinergics (excludes TCAs)");
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

        recommendation.addEntry(entry);
    }
}
