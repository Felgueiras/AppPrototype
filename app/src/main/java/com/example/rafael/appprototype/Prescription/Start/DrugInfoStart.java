package com.example.rafael.appprototype.Prescription.Start;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.Criteria.PrescriptionStart;
import com.example.rafael.appprototype.R;

/**
 * Created by felgueiras on 14/01/2017.
 */
public class DrugInfoStart extends Fragment {

    public static String DRUG;
    private PrescriptionStart drugInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drug_info_start, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            // get drug area
            drugInfo = (PrescriptionStart) bundle.getSerializable(DRUG);
        }

        TextView descriptionTextView = (TextView) view.findViewById(R.id.description);
        descriptionTextView.setText("Recommendation: " + drugInfo.getDescription());
        return view;
    }
}
