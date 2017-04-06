package com.felgueiras.apps.geriatric_helper.Prescription.Start;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.felgueiras.apps.geriatric_helper.DataTypes.Criteria.PrescriptionStart;
import com.felgueiras.apps.geriatric_helper.R;

import java.util.ArrayList;

/**
 * Created by felgueiras on 14/01/2017.
 */
public class DrugInfoStart extends Fragment {

    public static String DRUGS = "drugs";
    private ArrayList<PrescriptionStart> drugInfos;

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
            drugInfos = (ArrayList<PrescriptionStart>) bundle.getSerializable(DRUGS);
        }

        ListView startList = (ListView) view.findViewById(R.id.start_drugs_list_view);


        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_normal, R.id.drug_name,
                drugInfoAsRecommendation());
        startList.setAdapter(adapter);
        return view;
    }

    private ArrayList<String> drugInfoAsRecommendation() {
        ArrayList<String> recommendations = new ArrayList<>();
        for (PrescriptionStart prescriptionStart : drugInfos) {
            recommendations.add("Recommendation: " + prescriptionStart.getDescription());
        }

        return  recommendations;
    }
}
