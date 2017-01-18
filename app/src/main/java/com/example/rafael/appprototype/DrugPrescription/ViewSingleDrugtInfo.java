package com.example.rafael.appprototype.DrugPrescription;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.Criteria.BeersCriteria;
import com.example.rafael.appprototype.DataTypes.Criteria.RecommendationInfo;
import com.example.rafael.appprototype.DataTypes.Criteria.PrescriptionStart;
import com.example.rafael.appprototype.DataTypes.Criteria.StartCriteria;
import com.example.rafael.appprototype.DataTypes.Criteria.PrescriptionStopp;
import com.example.rafael.appprototype.DataTypes.Criteria.StoppCriteria;
import com.example.rafael.appprototype.DrugPrescription.Beers.DrugInfoBeers;
import com.example.rafael.appprototype.DrugPrescription.Start.DrugInfoStart;
import com.example.rafael.appprototype.DrugPrescription.Stopp.DrugInfoStopp;
import com.example.rafael.appprototype.EmptyStateFragment;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

/**
 * Created by rafael on 05-10-2016.
 */
public class ViewSingleDrugtInfo extends Fragment {

    public static final String DRUG = "drug";
    private String drug;
    private ArrayList<BeersCriteria> beersData;
    private ArrayList<StoppCriteria> stoppData;
    private ArrayList<StartCriteria> startData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.drug_info, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            // get drug name
            drug = bundle.getString(DRUG);
        }

        // access Views
        getActivity().setTitle(drug);

        // get drug info
        beersData = BeersCriteria.getBeersData();
        stoppData = StoppCriteria.getStoppData();
        startData = StartCriteria.getStartData();
        // stopp
        final ArrayList<String> stoppCriteriaDrugs = StoppCriteria.getAllDrugsStopp(stoppData);
        // beers
        final ArrayList<String> beersCriteriaDrugs = BeersCriteria.getAllDrugsBeers(beersData);
        // start
        final ArrayList<String> startCriteriaDrugs = StartCriteria.getAllDrugsStart(startData);

        // check info (start, stopp or beers)
        FragmentManager fragmentManager = getFragmentManager();
        // start
        if (startCriteriaDrugs.contains(drug)) {
            // get info about the Start criteria for that drug
            PrescriptionStart pr = StartCriteria.getStartCriteriaPresciptionForDrug(drug, startData);
            DrugInfoStart drugInfoStart = new DrugInfoStart();
            Bundle args = new Bundle();
            args.putSerializable(DrugInfoStart.DRUG, pr);
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
            PrescriptionStopp pr = StoppCriteria.getStoppCriteriaPresciptionForDrug(drug, stoppData);
            DrugInfoStopp drugInfoStopp = new DrugInfoStopp();
            Bundle args = new Bundle();
            args.putSerializable(DrugInfoStopp.DRUG, pr);
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
        // beers
        if (beersCriteriaDrugs.contains(drug)) {
            RecommendationInfo drugInfo = BeersCriteria.getBeersCriteriaInfoAboutDrug(drug, beersData);
            DrugInfoBeers drugInfoBeers = new DrugInfoBeers();
            Bundle args = new Bundle();
            args.putSerializable(DrugInfoBeers.DRUG, drugInfo);
            drugInfoBeers.setArguments(args);
            fragmentManager.beginTransaction()
                    .replace(R.id.drug_info_beers, drugInfoBeers)
                    .commit();
        }
        else {
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

}

