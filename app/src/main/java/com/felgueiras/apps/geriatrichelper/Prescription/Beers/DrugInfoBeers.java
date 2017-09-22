package com.felgueiras.apps.geriatrichelper.Prescription.Beers;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felgueiras.apps.geriatrichelper.DataTypes.Criteria.RecommendationInfo;
import com.felgueiras.apps.geriatrichelper.R;

import java.util.ArrayList;

/**
 * Created by felgueiras on 14/01/2017.
 */
public class DrugInfoBeers extends Fragment {

    public static String DRUG = "drug";
    public static String DRUGS = "drugs";
    private RecommendationInfo drugInfo;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            ArrayList<RecommendationInfo> drugInfos = (ArrayList<RecommendationInfo>) bundle.getSerializable(DRUGS);
            view = inflater.inflate(R.layout.drug_info_beers_multiple, container, false);

            // fill the RecyclerView
            RecyclerView recommendationIndoRecyclerView = view.findViewById(R.id.beers_criteria_recycler_view);

            // display card for each Patientndroid rec
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recommendationIndoRecyclerView.setLayoutManager(mLayoutManager);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recommendationIndoRecyclerView.getContext(),
                    layoutManager.getOrientation());
            recommendationIndoRecyclerView.addItemDecoration(dividerItemDecoration);
            BeersSingleDrugInfo adapter = new BeersSingleDrugInfo(getActivity(), drugInfos);
            recommendationIndoRecyclerView.setAdapter(adapter);

        }


        return view;
    }
}
