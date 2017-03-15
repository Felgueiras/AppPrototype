package com.example.rafael.appprototype.Prescription.Stopp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.appprototype.DataTypes.Criteria.PrescriptionStopp;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

/**
 * Created by felgueiras on 14/01/2017.
 */
public class DrugInfoStopp extends Fragment {


    public static String DRUGS = "drugs";
    private ArrayList<PrescriptionStopp> drugInfos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drug_info_stopp_multiple, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            // get drug area
            drugInfos = (ArrayList<PrescriptionStopp>) bundle.getSerializable(DRUGS);
        }

        // fill the RecyclerView
        RecyclerView recommendationIndoRecyclerView = (RecyclerView) view.findViewById(R.id.stopp_criteria_recycler_view);

        // display card for each Patientndroid rec
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recommendationIndoRecyclerView.setLayoutManager(mLayoutManager);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recommendationIndoRecyclerView.getContext(),
                layoutManager.getOrientation());
        recommendationIndoRecyclerView.addItemDecoration(dividerItemDecoration);
        StoppSingleDrugInfo adapter = new StoppSingleDrugInfo(getActivity(), drugInfos);
        recommendationIndoRecyclerView.setAdapter(adapter);


        return view;
    }
}
