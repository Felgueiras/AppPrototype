package com.example.rafael.appprototype.DrugPrescription;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rafael.appprototype.DrugPrescription.BeersCriteria.BeersRecommendation;
import com.example.rafael.appprototype.DrugPrescription.BeersCriteria.RecommendationInfo;
import com.example.rafael.appprototype.DrugPrescription.BeersCriteria.TherapeuticCategoryEntry;
import com.example.rafael.appprototype.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DrugPrescription extends AppCompatActivity {

    // List view
    private ListView lv, selectedItemsListView;

    // Listview Adapter
    ArrayAdapter<String> adapter, itemsAdapter;

    // Search EditText
    EditText inputSearch;

    TextView selectedDrugInfo;


    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;

    ArrayList<StoppCriterion> stopCriteria;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private BeersRecommendation recommendation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // add start, stopp and beers criteria
        addStartStoppBeersData();

        /**
         * Set the items on the GUI
         */
        // Listview Data
        final Set<String> selected = new HashSet<>();

        selectedDrugInfo = (TextView) findViewById(R.id.selectedDrugInfo);

        // list view with possible choices
        lv = (ListView) findViewById(R.id.list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearch);

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


        final String testType = "beers";
        if (testType.equals("stopp")) {
            // stopp Criteria
            adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.product_name, stoppCriteriaDrugs);
        } else if (testType.equals("beers")) {
            // Beers criteria
            adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.product_name, beersCriteriaDrugs);
        }
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDrug;

                if (testType.equals("stopp")) {
                    selectedDrug = stoppCriteriaDrugs.get(position);
                    // get info about the Stopp criteria for that drug
                    selectedDrugInfo.setText("");
                    Prescription pr = getStoppCriteriaPresciptionForDrug(selectedDrug);
                    //itemsAdapter.notifyDataSetChanged();
                    if (pr != null) {
                        ArrayList<Issue> situations = pr.getSituations();
                        String drugInfo = "";
                        for (Issue issue : situations) {
                            Log.d("Search", String.valueOf(issue));
                            drugInfo += issue.toString() + "\n";
                        }
                        selectedDrugInfo.setText(selectedDrug);
                        selectedDrugInfo.append(drugInfo);
                    }
                } else if (testType.equals("beers")) {
                    selectedDrug = beersCriteriaDrugs.get(position);
                    RecommendationInfo drugInfo = getBeersCriteriaInfoAboutDrug(selectedDrug);
                    if (drugInfo != null)
                    {
                        selectedDrugInfo.setText(drugInfo.toString());
                    }
                }

            }


        });

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                DrugPrescription.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * Get the StoppCriteria for a given drug.
     *
     * @param drugSearchingFor
     * @return
     */
    public Prescription getStoppCriteriaPresciptionForDrug(String drugSearchingFor) {
        for (StoppCriterion criterion : stopCriteria) {
            ArrayList<Prescription> prescriptions = criterion.getPrescriptions();
            for (Prescription pr : prescriptions) {
                String drugName = pr.getDrugName();
                if (drugName.equals(drugSearchingFor)) {
                    Log.d("Drugs", "Found");
                    return pr;
                }
            }
        }
        return null;
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
        stopCriteria = new ArrayList<>();
        stopCriteria.add(urogenitalSystem);

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


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("DrugPrescription Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}