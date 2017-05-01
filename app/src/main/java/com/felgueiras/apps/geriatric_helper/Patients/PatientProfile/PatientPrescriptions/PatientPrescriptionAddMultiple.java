package com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientPrescriptions;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.Criteria.Beers.BeersCriteria;
import com.felgueiras.apps.geriatric_helper.DataTypes.Criteria.StartCriteria;
import com.felgueiras.apps.geriatric_helper.DataTypes.Criteria.StoppCriteria;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PrescriptionFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.BackStackHandler;
import com.felgueiras.apps.geriatric_helper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatric_helper.PatientsManagement;
import com.felgueiras.apps.geriatric_helper.Prescription.AllDrugs.DataHelperDrugs;
import com.felgueiras.apps.geriatric_helper.Prescription.AllDrugs.DrugListItem;
import com.felgueiras.apps.geriatric_helper.Prescription.AllDrugs.DrugSuggestion;
import com.felgueiras.apps.geriatric_helper.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Display the list of Patients to view them or select one of them.
 */
public class PatientPrescriptionAddMultiple extends Fragment {

    public static final String PATIENT = "PATIENT";
    private RecyclerView mSearchResultsList;
    private FloatingSearchView mSearchView;
    private String TAG = "Search";

    private boolean mIsDarkSearchTheme = false;


    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;


    private String mLastQuery = "";
    private PatientFirebase patient;
    private Activity context;
    private AddDrugListItemAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    private void setupFloatingSearch() {
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {

                    //this shows the top left circular progress
                    //you can call it where ever you want, but
                    //it makes sense to do it when loading something in
                    //the background.
                    mSearchView.showProgress();

                    //simulates a query call to a data source
                    //with a new query.
                    DataHelperDrugs.findSuggestions(getActivity(), newQuery, 5,
                            FIND_SUGGESTION_SIMULATED_DELAY, new DataHelperDrugs.OnFindSuggestionsListener() {

                                @Override
                                public void onResults(List<DrugSuggestion> results) {

                                    //this will swap the data and
                                    //render the collapse/expand animations as necessary
                                    mSearchView.swapSuggestions(results);

                                    //let the users know that the background
                                    //process has completed
                                    mSearchView.hideProgress();
                                }
                            });
                }

            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {

                DrugSuggestion colorSuggestion = (DrugSuggestion) searchSuggestion;
                // add to history
                ((DrugSuggestion) searchSuggestion).setIsHistory(true);

                mLastQuery = searchSuggestion.getBody();

                Fragment endFragment = new PatientPrescriptionCreate();
                Bundle args = new Bundle();
                args.putString(PatientPrescriptionCreate.DRUG, colorSuggestion.getDrugName());
                FragmentTransitions.replaceFragment(getActivity(), endFragment, args, Constants.tag_view_drug_info);
            }

            @Override
            public void onSearchAction(String query) {
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {

                //show suggestions when search bar gains focus (typically history suggestions)
                mSearchView.swapSuggestions(DataHelperDrugs.getHistory(getActivity(), 3));
                Log.d(TAG, "onFocus()");
            }

            @Override
            public void onFocusCleared() {

                //set the title of the bar so that when focus is returned a new query begins
                mSearchView.setSearchBarTitle(mLastQuery);

                //you can also set setSearchText(...) to make keep the query there when not focused and when focus returns
                //mSearchView.setSearchText(searchSuggestion.getBody());

                Log.d(TAG, "onFocusCleared()");
            }
        });


        //use this listener to listen to menu clicks when app:floatingSearch_leftAction="showHome"
        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {
                Log.d(TAG, "onHomeClicked()");
            }
        });

        /*
         * Here you have access to the left icon and the text of a given suggestion
         * item after as it is bound to the suggestion list. You can utilize this
         * callback to change some properties of the left icon and the text. For example, you
         * can load the left icon images using your favorite image loading library, or change text color.
         *
         *
         * Important:
         * Keep in mind that the suggestion list is a RecyclerView, so views are reused for different
         * items in the list.
         */
        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                                         TextView textView, SearchSuggestion item, int itemPosition) {
                DrugSuggestion drugSuggestion = (DrugSuggestion) item;

                String textColor = mIsDarkSearchTheme ? "#ffffff" : "#000000";
                String textLight = mIsDarkSearchTheme ? "#bfbfbf" : "#787878";

                if (drugSuggestion.getIsHistory()) {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_history_black_24dp, null));

                    Util.setIconColor(leftIcon, Color.parseColor(textColor));
                    leftIcon.setAlpha(.36f);
                } else {
                    leftIcon.setAlpha(0.0f);
                    leftIcon.setImageDrawable(null);
                }

                textView.setTextColor(Color.parseColor(textColor));
                String text = drugSuggestion.getBody()
                        .replaceFirst(mSearchView.getQuery(),
                                "<font color=\"" + textLight + "\">" + mSearchView.getQuery() + "</font>");
                textView.setText(Html.fromHtml(text));
            }

        });

        //listen for when suggestion list expands/shrinks in order to move down/up the
        //search results list
        mSearchView.setOnSuggestionsListHeightChanged(new FloatingSearchView.OnSuggestionsListHeightChanged() {
            @Override
            public void onSuggestionsListHeightChanged(float newHeight) {
                mSearchResultsList.setTranslationY(newHeight);
            }
        });
    }

    private void setupResultsList() {
        DrugListItem mSearchResultsAdapter = new DrugListItem(getActivity(),
                Constants.allDrugs,
                true,
                patient);
        mSearchResultsList.setAdapter(mSearchResultsAdapter);
        mSearchResultsList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.prescription_add_main, container, false);

        context = getActivity();


        RecyclerView drugAdd = (RecyclerView) view.findViewById(R.id.drugItems);

        ArrayList<StoppCriteria> stoppGeneral = StoppCriteria.getStoppCriteria();
        ArrayList<StartCriteria> startGeneral = StartCriteria.getStartCriteria();

        // stopp
        final ArrayList<String> stoppCriteriaDrugs = StoppCriteria.getAllDrugsStopp(stoppGeneral);
        // beers - organ system
        final ArrayList<String> beersDrugsOrganSystem = BeersCriteria.getBeersDrugsByOrganSystemString();
        // beers - disease
        final ArrayList<String> beersDrugsDisease = BeersCriteria.getBeersDrugsByDiseaseString();
        // beers - drugs
        final ArrayList<String> beersDrugsNoCategory = BeersCriteria.getBeersDrugsNamesString();
        // start
        final ArrayList<String> startCriteriaDrugs = StartCriteria.getAllDrugsStart(startGeneral);


        // mix into a single ArrayList
        Constants.allDrugs.addAll(stoppCriteriaDrugs);
        Constants.allDrugs.addAll(startCriteriaDrugs);
        Constants.allDrugs.addAll(beersDrugsDisease);
        Constants.allDrugs.addAll(beersDrugsOrganSystem);
        Constants.allDrugs.addAll(beersDrugsNoCategory);

        // remove duplicates
        Set<String> hs = new HashSet<>();
        hs.addAll(Constants.allDrugs);
        Constants.allDrugs.clear();
        Constants.allDrugs.addAll(hs);
        // sort list
        Collections.sort(Constants.allDrugs);


        // display card for each Patientndroid rec
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        drugAdd.setLayoutManager(mLayoutManager);
        adapter = new AddDrugListItemAdapter(getActivity(), Constants.allDrugs, patient);
        drugAdd.setAdapter(adapter);

        Button savePrescriptions = (Button) view.findViewById(R.id.saveButton);
        savePrescriptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add drugs to patient
                ArrayList<String> addedDrugs = adapter.getAddedDrugsList();
                ArrayList<String> addedNotes = adapter.getAddedDrugsNotes();

                // get current date
                Calendar c = Calendar.getInstance();
                Date prescriptionDate = c.getTime();
                for (int i = 0; i < addedDrugs.size(); i++) {
                    String currentDrug = addedDrugs.get(i);
                    String currentNote;
                    if (addedNotes.size() > 0)
                        currentNote = addedNotes.get(i);
                    else
                        currentNote = null;
//                    if (currentDrug.getText().length() == 0) {
//                        Snackbar.make(getView(), R.string.add_prescription_error_name, Snackbar.LENGTH_SHORT).show();
//                        return;
//                    }

                    // create new Prescription object
                    PrescriptionFirebase prescription = new PrescriptionFirebase(currentDrug,
                            currentNote, prescriptionDate);
                    prescription.setGuid("PRESCRIPTION" + new Random().nextInt());
                    prescription.setPatientID(patient.getGuid());
                    patient.addPrescription(prescription.getGuid(), context);

                    // update patient
                    PatientsManagement.getInstance().updatePatient(patient, context);

                    // save Prescription
                    FirebaseDatabaseHelper.createPrescription(prescription);

                }
                Snackbar.make(getView(), R.string.add_prescription_success, Snackbar.LENGTH_SHORT).show();
                BackStackHandler.getFragmentManager().popBackStack();
            }
        });


        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        // check if there is an associated patient
        if (getArguments() != null && getArguments().containsKey(PATIENT))
            patient = (PatientFirebase) getArguments().getSerializable(PATIENT);

        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
//        inflater.inflate(R.menu.menu_save, menu);
    }


}

