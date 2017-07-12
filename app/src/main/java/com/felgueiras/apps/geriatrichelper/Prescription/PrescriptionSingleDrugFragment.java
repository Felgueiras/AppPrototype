package com.felgueiras.apps.geriatrichelper.Prescription;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felgueiras.apps.geriatrichelper.DataTypes.Criteria.Beers.BeersCriteria;
import com.felgueiras.apps.geriatrichelper.DataTypes.Criteria.RecommendationInfo;
import com.felgueiras.apps.geriatrichelper.DataTypes.Criteria.PrescriptionStart;
import com.felgueiras.apps.geriatrichelper.DataTypes.Criteria.StartCriteria;
import com.felgueiras.apps.geriatrichelper.DataTypes.Criteria.PrescriptionStopp;
import com.felgueiras.apps.geriatrichelper.DataTypes.Criteria.StoppCriteria;
import com.felgueiras.apps.geriatrichelper.Prescription.Beers.DrugInfoBeers;
import com.felgueiras.apps.geriatrichelper.Prescription.Start.DrugInfoStart;
import com.felgueiras.apps.geriatrichelper.Prescription.Stopp.DrugInfoStopp;
import com.felgueiras.apps.geriatrichelper.EmptyStateFragment;
import com.felgueiras.apps.geriatrichelper.R;

import java.util.ArrayList;

/**
 * Created by rafael on 05-10-2016.
 */
public class PrescriptionSingleDrugFragment extends Fragment {

    public static final String DRUG = "drug";
    private String drug;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.drug_info, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            // get drug area
            drug = bundle.getString(DRUG);
        }

        // access Views
        getActivity().setTitle(drug);

        // get drug info
        ArrayList<StoppCriteria> stoppData = StoppCriteria.getStoppCriteria();
        ArrayList<StartCriteria> startData = StartCriteria.getStartCriteria();
        // stopp
        final ArrayList<String> stoppCriteriaDrugs = StoppCriteria.getAllDrugsStopp(stoppData);
        // beers
        final ArrayList<String> beersCriteriaDrugs = BeersCriteria.getBeersDrugsAllString();
        // start
        final ArrayList<String> startCriteriaDrugs = StartCriteria.getAllDrugsStart(startData);

        // check info (start, stopp or beers)
        FragmentManager fragmentManager = getChildFragmentManager();
        // start
        if (startCriteriaDrugs.contains(drug)) {
            // get info about the Start criteria for that drug
            ArrayList<PrescriptionStart> pr = StartCriteria.getStartCriteriaPresciptionForDrug(drug, startData);
            DrugInfoStart drugInfoStart = new DrugInfoStart();
            Bundle args = new Bundle();
            args.putSerializable(DrugInfoStart.DRUGS, pr);
            drugInfoStart.setArguments(args);
            fragmentManager.beginTransaction()
                    .replace(R.id.drug_info_start, drugInfoStart)
                    .commit();
        } else {
            Fragment fragment = new EmptyStateFragment();
            Bundle args = new Bundle();
            args.putString(EmptyStateFragment.MESSAGE, getResources().getString(R.string.no_start_criteria));
            fragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .replace(R.id.drug_info_start, fragment)
                    .commit();
        }
        // stopp
        if (stoppCriteriaDrugs.contains(drug)) {
            // get info about the Stopp criteria for that drug
            ArrayList<PrescriptionStopp> pr = StoppCriteria.getStoppCriteriaPresciptionForDrug(drug, stoppData);
            DrugInfoStopp drugInfoStopp = new DrugInfoStopp();
            Bundle args = new Bundle();
            args.putSerializable(DrugInfoStopp.DRUGS, pr);
            drugInfoStopp.setArguments(args);
            fragmentManager.beginTransaction()
                    .replace(R.id.drug_info_stopp, drugInfoStopp)
                    .commit();
        } else {
            Fragment fragment = new EmptyStateFragment();
            Bundle args = new Bundle();
            args.putString(EmptyStateFragment.MESSAGE, getResources().getString(R.string.no_stopp_criteria));
            fragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .replace(R.id.drug_info_stopp, fragment)
                    .commit();
        }
        /**
         * Beers.
         */
        if (beersCriteriaDrugs.contains(drug)) {
            ArrayList<RecommendationInfo> drugInfos = BeersCriteria.getBeersCriteria(drug);
            DrugInfoBeers drugInfoBeers = new DrugInfoBeers();
            Bundle args = new Bundle();
            args.putSerializable(DrugInfoBeers.DRUGS, drugInfos);
            drugInfoBeers.setArguments(args);
            fragmentManager.beginTransaction()
                    .replace(R.id.drug_info_beers, drugInfoBeers)
                    .commit();
        } else {
            // check if it's a drug or a drug category
            Fragment fragment = new EmptyStateFragment();
            Bundle args = new Bundle();
            args.putString(EmptyStateFragment.MESSAGE, getResources().getString(R.string.no_beers_criteria));
            fragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .replace(R.id.drug_info_beers, fragment)
                    .commit();
        }
        return view;
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

