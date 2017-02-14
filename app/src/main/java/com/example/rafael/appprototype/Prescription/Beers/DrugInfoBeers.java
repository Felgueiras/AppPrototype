package com.example.rafael.appprototype.Prescription.Beers;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.Criteria.RecommendationInfo;
import com.example.rafael.appprototype.R;

/**
 * Created by felgueiras on 14/01/2017.
 */
public class DrugInfoBeers extends Fragment {

    public static String DRUG;
    private RecommendationInfo drugInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drug_info_beers, container, false);
        System.out.println("Beers criteria");
        Bundle bundle = getArguments();
        if (bundle != null) {
            // get drug area
            drugInfo = (RecommendationInfo) bundle.getSerializable(DRUG);
        }

        // get the views
        TextView recommendation = (TextView) view.findViewById(R.id.recommendation);
        TextView rationale = (TextView) view.findViewById(R.id.rationale);
        TextView qualityOfEvidence = (TextView) view.findViewById(R.id.qualityOfEvidence);
        TextView strengthOfRecommendation = (TextView) view.findViewById(R.id.strengthOfRecommendation);

        // set the views
        recommendation.setText(drugInfo.getRecommendation());
        rationale.setText(drugInfo.getRationale());
        qualityOfEvidence.setText(drugInfo.getQualityOfEvidence());
        strengthOfRecommendation.setText(drugInfo.getStrengthOfRecommendation());

        return view;
    }
}
