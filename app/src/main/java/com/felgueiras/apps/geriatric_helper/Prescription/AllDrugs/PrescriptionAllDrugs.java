package com.felgueiras.apps.geriatric_helper.Prescription.AllDrugs;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
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
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseStorageHelper;
import com.felgueiras.apps.geriatric_helper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatric_helper.Prescription.PrescriptionSingleDrugFragment;
import com.felgueiras.apps.geriatric_helper.R;
import com.futuremind.recyclerviewfastscroll.FastScroller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Display the list of Patients to view them or select one of them.
 */
public class PrescriptionAllDrugs extends Fragment {

    private static final String BUNDLE_RECYCLER_LAYOUT = "abc";
    private RecyclerView mSearchResultsList;
    private FloatingSearchView mSearchView;
    private String TAG = "Search";

    private boolean mIsDarkSearchTheme = false;


    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;


    private String mLastQuery = "";


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

                Fragment endFragment = new PrescriptionSingleDrugFragment();
                Bundle args = new Bundle();
                args.putString(PrescriptionSingleDrugFragment.DRUG, colorSuggestion.getDrugName());
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
        DrugListItem mSearchResultsAdapter = new DrugListItem(getActivity(), Constants.allDrugs, false, null);
        mSearchResultsList.setAdapter(mSearchResultsAdapter);
        mSearchResultsList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_drugs_list_persistent_search, container, false);

        ArrayList<StoppCriteria> stoppGeneral = FirebaseStorageHelper.getInstance().getStoppCriteria();
        ArrayList<StartCriteria> startGeneral = FirebaseStorageHelper.getInstance().getStartCriteria();

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


        // fill the RecyclerView
        mSearchResultsList = (RecyclerView) view.findViewById(R.id.drugs_recycler_view);
        FastScroller fastScroller = (FastScroller) view.findViewById(R.id.fastscroll);

        // display card for each Patientndroid rec
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mSearchResultsList.setLayoutManager(mLayoutManager);
        DrugListItem adapter = new DrugListItem(getActivity(), Constants.allDrugs, false, null);
        mSearchResultsList.setAdapter(adapter);

        fastScroller.setRecyclerView(mSearchResultsList);


        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mSearchView = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
        mSearchResultsList = (RecyclerView) view.findViewById(R.id.drugs_recycler_view);

        setupFloatingSearch();
        setupResultsList();

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (Constants.patientsListBundle != null) {
            Parcelable savedRecyclerLayoutState = Constants.patientsListBundle.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mSearchResultsList.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Constants.patientsListBundle = new Bundle();
        Constants.patientsListBundle.putParcelable(BUNDLE_RECYCLER_LAYOUT, mSearchResultsList.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
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
}

