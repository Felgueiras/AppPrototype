package com.example.rafael.appprototype.DrugPrescription.StartStopp;

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

import com.example.rafael.appprototype.DrugPrescription.BeersCriteria.BeersRecommendation;
import com.example.rafael.appprototype.DrugPrescription.BeersCriteria.RecommendationInfo;
import com.example.rafael.appprototype.DrugPrescription.BeersCriteria.TherapeuticCategoryEntry;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

public class StoppCriteriaFragment extends Fragment {

    // List view
    private ListView drugsSearchList, selectedDrugsList;

    // Listview Adapter
    ArrayAdapter<String> drugsListAdapter;

    // Search EditText
    EditText inputSearch;

    private BeersRecommendation recommendation;

    ArrayList<Prescription> selectedPrescriptions = new ArrayList<>();
    private StoppGeneral stoppGeneral;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addStoppBeersData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.stopp_criteria, container, false);

        // list view with possible choices and selected ones
        drugsSearchList = (ListView) v.findViewById(R.id.list_view);
        selectedDrugsList = (ListView) v.findViewById(R.id.selectedDrugs);
        inputSearch = (EditText) v.findViewById(R.id.inputSearch);

        final ArrayList<String> stoppCriteriaDrugs = getAllDrugsStopp(stoppGeneral);
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
        final SelectedDrugsListAdapter customAdapter = new SelectedDrugsListAdapter(getActivity(), selectedPrescriptions, stoppGeneral);
        selectedDrugsList.setAdapter(customAdapter);
        /**
         * Item was selected from list.
         */
        drugsSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDrug;

                if (testType.equals("stopp")) {
                    TextView textView = (TextView) view.findViewById(R.id.drug_name);
                    selectedDrug = (String) textView.getText();
                    // get info about the Stopp criteria for that drug
                    Prescription pr = stoppGeneral.getStoppCriteriaPresciptionForDrug(selectedDrug);
                    String drugInfo = "";
                    if (pr != null) {
                        ArrayList<Issue> situations = pr.getIssues();

                        for (Issue issue : situations) {
                            drugInfo += issue.toString() + "\n";
                        }

                    }
                    selectedPrescriptions.add(pr);
                    customAdapter.notifyDataSetChanged();
                } else if (testType.equals("beers")) {
                    selectedDrug = beersCriteriaDrugs.get(position);
                    RecommendationInfo drugInfo = getBeersCriteriaInfoAboutDrug(selectedDrug);
                    if (drugInfo != null) {

                    }
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

    /**
     * Add Stopp and Beers data.
     */
    private void addStoppBeersData() {
        /**
         * Stopp criteria.
         */
        // Urogenital
        StoppCriterion urogenitalSystem = new StoppCriterion("Urogenital System");
        Prescription pr = new Prescription("Bladder antimuscarinic drugs");
        Issue issue = new Issue("with dementia", "risk of increased confusion, agitation");
        // add Issue to Prescription
        pr.addIssue(issue);
        pr.addIssue(new Issue("with chronic glaucoma", "risk of acute exacerbation of glaucoma"));
        // add Prescription to StoppCriterion
        urogenitalSystem.addPrescription(pr);
        pr = new Prescription("Alpha-blockers");
        issue = new Issue("in males with frequent incontinence i.e. one or more episodes\n" +
                "of incontinence daily",
                "risk of urinary frequency and worsening\n" +
                        "of incontinence");
        pr.addIssue(issue);
        urogenitalSystem.addPrescription(pr);
        stoppGeneral = new StoppGeneral();
        stoppGeneral.addStoppCriterion(urogenitalSystem);
        // Endocrine
        StoppCriterion endocrineSystem = new StoppCriterion("Endocrine System");
        pr = new Prescription("Glibenclamide or chlorpropamide");
        issue = new Issue("with type 2 diabetes\n" +
                "mellitus", "risk of prolonged hypoglycaemia");
        // add Issue to Prescription
        pr.addIssue(issue);
        // add Prescription to StoppCriterion
        endocrineSystem.addPrescription(pr);
        pr = new Prescription("Oestrogens");
        issue = new Issue("with a history of breast cancer or venous thromboembolism",
                "ncreased risk of recurrence");
        pr.addIssue(issue);
        endocrineSystem.addPrescription(pr);
        stoppGeneral.addStoppCriterion(endocrineSystem);
        // Musculoskeletal
        StoppCriterion musculoskeletalSystem = new StoppCriterion("Musculoskeletal System");
        pr = new Prescription("Non-steroidal anti-inflammatory drug (NSAID)");
        issue = new Issue("with history of peptic ulcer disease or gastrointestinal bleeding, unless\n" +
                "with concurrent histamine H2 receptor antagonist, PPI or misoprostol",
                "risk of peptic ulcer relapse");
        // add Issue to Prescription
        pr.addIssue(issue);
        musculoskeletalSystem.addPrescription(pr);
        stoppGeneral.addStoppCriterion(musculoskeletalSystem);
        // Gastrointestinal
        StoppCriterion gastrointestinalSystem = new StoppCriterion("Gastrointestinal System");
        pr = new Prescription("Diphenoxylate, loperamide or codeine phosphate");
        issue = new Issue("for treatment of severe infective gastroenteritis i.e. bloody " +
                "diarrhoea, high fever or severe systemic toxicity",
                "risk of exacerbation or protraction of infection");
        // add Issue to Prescription
        pr.addIssue(issue);
        gastrointestinalSystem.addPrescription(pr);
        stoppGeneral.addStoppCriterion(gastrointestinalSystem);
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
