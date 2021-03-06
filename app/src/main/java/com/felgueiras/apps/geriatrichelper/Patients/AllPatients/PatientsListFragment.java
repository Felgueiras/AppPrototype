package com.felgueiras.apps.geriatrichelper.Patients.AllPatients;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.felgueiras.apps.geriatrichelper.Constants;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatrichelper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatrichelper.Main.PrivateAreaActivity;
import com.felgueiras.apps.geriatrichelper.Patients.AllPatients.FloatingSearch.DataHelper;
import com.felgueiras.apps.geriatrichelper.Patients.AllPatients.FloatingSearch.PersonSuggestion;
import com.felgueiras.apps.geriatrichelper.Patients.NewPatient.CreatePatientFragment;
import com.felgueiras.apps.geriatrichelper.Patients.PatientProfile.PatientProfileFragment;
import com.felgueiras.apps.geriatrichelper.PatientsManagement;
import com.felgueiras.apps.geriatrichelper.R;
import com.futuremind.recyclerviewfastscroll.FastScroller;

import java.util.ArrayList;
import java.util.List;

/**
 * Display the list of Patients to view them or select one of them.
 */
public class PatientsListFragment extends Fragment {

    private static final String BUNDLE_RECYCLER_LAYOUT = "abc";
    public static String selectPatient = "selectPatient";
    private RecyclerView mSearchResultsList;
    private FloatingSearchView mSearchView;
    private String TAG = "Search";
    ArrayList<PatientFirebase> patients = new ArrayList<>();


    private boolean mIsDarkSearchTheme = false;


    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;


    private String mLastQuery = "";


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mSearchView = view.findViewById(R.id.floating_search_view);
        mSearchResultsList = view.findViewById(R.id.patients_recycler_view);

        setupFloatingSearch();
        setupResultsList();

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {


        setupFloatingSearch();
        setupResultsList();

        Log.d("Patients","Resuming");

        super.onResume();
    }

    /**
     * Setup searching mechanism.
     */
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
                    DataHelper.findSuggestions(getActivity(), newQuery, 5,
                            FIND_SUGGESTION_SIMULATED_DELAY, new DataHelper.OnFindSuggestionsListener() {

                                @Override
                                public void onResults(List<PersonSuggestion> results) {

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

                PersonSuggestion person = (PersonSuggestion) searchSuggestion;
                // add to history
                ((PersonSuggestion) searchSuggestion).setIsHistory(true);
//                DataHelper.findColors(getActivity(), colorSuggestion.getBody(),
//                        new DataHelper.OnFindColorsListener() {
//
//                            @Override
//                            public void onResults(List<ColorWrapper> results) {
//                                Log.d(TAG, "Size: " + results.size());
//                                mSearchResultsAdapter.swapData(results);
//                            }
//
//                        });
//                mSearchResultsAdapter = new PatientCardPatientsList(getActivity(), Patient.getAllPatients());
//                Log.d(TAG, "onSuggestionClicked()");

                mLastQuery = searchSuggestion.getBody();


                Fragment endFragment = new PatientProfileFragment();


                Bundle args = new Bundle();
//                args.putString("ACTION", holder.questionTextView.getText().toString());
//                args.putString("TRANS_TEXT", patientTransitionName);
                args.putSerializable(PatientProfileFragment.PATIENT, person.getPatient());
                ((PrivateAreaActivity) getActivity()).replaceFragmentSharedElements(endFragment, args,
                        Constants.tag_view_patient_info_records,
                        null);

            }

            @Override
            public void onSearchAction(String query) {
//                mLastQuery = query;
//
//                DataHelper.findColors(getActivity(), query,
//                        new DataHelper.OnFindColorsListener() {
//
//                            @Override
//                            public void onResults(List<ColorWrapper> results) {
//                                mSearchResultsAdapter.swapData(results);
//                            }
//
//                        });
//                Log.d(TAG, "onSearchAction()");
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {

                //show suggestions when search bar gains focus (typically history suggestions)
                mSearchView.swapSuggestions(DataHelper.getHistory(getActivity(), 3));
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
                PersonSuggestion colorSuggestion = (PersonSuggestion) item;

                String textColor = mIsDarkSearchTheme ? "#ffffff" : "#000000";
                String textLight = mIsDarkSearchTheme ? "#bfbfbf" : "#787878";

                if (colorSuggestion.getIsHistory()) {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_history_black_24dp, null));

                    Util.setIconColor(leftIcon, Color.parseColor(textColor));
                    leftIcon.setAlpha(.36f);
                } else {
                    leftIcon.setAlpha(0.0f);
                    leftIcon.setImageDrawable(null);
                }

                textView.setTextColor(Color.parseColor(textColor));
                String text = colorSuggestion.getBody()
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

    /**
     * Show the list of results (original/filtered)
     */
    private void setupResultsList() {
        PatientCardPatientsList mSearchResultsAdapter = new PatientCardPatientsList(getActivity(), PatientsManagement.getInstance().getPatients(getActivity()));
        mSearchResultsList.setAdapter(mSearchResultsAdapter);
        mSearchResultsList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_patients_list_persistent_search, container, false);

        // read arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.getBoolean(selectPatient, false)) {
                Log.d("Patient", "Going to select PATIENT");
//                Constants.selectPatient = true;
            }
        }

        // fill the RecyclerView
        mSearchResultsList = view.findViewById(R.id.patients_recycler_view);
        FastScroller fastScroller = view.findViewById(R.id.fastscroll);

        // display card for each Patientndroid rec
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mSearchResultsList.setLayoutManager(mLayoutManager);
        PatientCardPatientsList adapter = new PatientCardPatientsList(getActivity(), patients);
        mSearchResultsList.setAdapter(adapter);

        fastScroller.setRecyclerView(mSearchResultsList);


        // FAB
        final FloatingActionButton fab = view.findViewById(R.id.patients_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // create a new Patient - switch to CreatePatientFragment Fragment
                Bundle args = new Bundle();
                FragmentTransitions.replaceFragment(getActivity(), new CreatePatientFragment(), args, Constants.tag_create_patient);
            }
        });

        // hide/show FAB when scrolling
        mSearchResultsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fab.isShown()) {
                    fab.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        return view;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
}

