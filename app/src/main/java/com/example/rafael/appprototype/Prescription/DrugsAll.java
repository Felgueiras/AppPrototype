package com.example.rafael.appprototype.Prescription;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.Criteria.Beers.BeersCriteria;
import com.example.rafael.appprototype.DataTypes.Criteria.StartCriteria;
import com.example.rafael.appprototype.DataTypes.Criteria.StoppCriteria;
import com.example.rafael.appprototype.R;
import com.futuremind.recyclerviewfastscroll.FastScroller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Search a drug by its area.
 */
public class DrugsAll extends Fragment {

    // List view
    private RecyclerView drugsRecyclerView;

    // Listview Adapter
    DrugListItem adapter;

    /**
     * Stopp criteria.
     */
    private ArrayList<StoppCriteria> stoppGeneral;

    /**
     * Start criteria.
     */
    private ArrayList<StartCriteria> startGeneral;
    private Parcelable state;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get all the criterias
        stoppGeneral = StoppCriteria.getStoppData();
        startGeneral = StartCriteria.getStartData();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.search_drugs, container, false);

        // list view with possible choices and selected ones
        drugsRecyclerView = (RecyclerView) view.findViewById(R.id.list_view);

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
        ArrayList<String> allDrugs = new ArrayList<>();
        allDrugs.addAll(stoppCriteriaDrugs);
        allDrugs.addAll(startCriteriaDrugs);
        allDrugs.addAll(beersDrugsDisease);
        allDrugs.addAll(beersDrugsOrganSystem);
        allDrugs.addAll(beersDrugsNoCategory);

        // remove duplicates
        Set<String> hs = new HashSet<>();
        hs.addAll(allDrugs);
        allDrugs.clear();
        allDrugs.addAll(hs);
        // sort list
        Collections.sort(allDrugs);

        FastScroller fastScroller = (FastScroller) view.findViewById(R.id.fastscroll);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        drugsRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new DrugListItem(getActivity(), allDrugs);

        drugsRecyclerView.setAdapter(adapter);
        fastScroller.setRecyclerView(drugsRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                layoutManager.getOrientation());
        drugsRecyclerView.addItemDecoration(dividerItemDecoration);
//        drugsRecyclerView.setFastScrollEnabled(true);

//        final FloatingSearchView floatingSearchView = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
//        drugsRecyclerView.setLayoutManager(mLayoutManager);
//        floatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
//            @Override
//            public void onSearchTextChanged(String oldQuery, final String newQuery) {
//
//                //get suggestions based on newQuery
//
//                //pass them on to the search view
////                floatingSearchView.swapSuggestions(newSuggestions);
//                Log.d("Search",oldQuery+"-"+newQuery);
//            }
//        });


        return view;
    }

    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (Constants.drugsListBundle != null) {
            Parcelable savedRecyclerLayoutState = Constants.drugsListBundle.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            drugsRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Constants.drugsListBundle = new Bundle();
        Constants.drugsListBundle.putParcelable(BUNDLE_RECYCLER_LAYOUT, drugsRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_search_info, menu);

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
