package com.example.rafael.appprototype.Prescription.Stopp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.rafael.appprototype.DataTypes.Criteria.PrescriptionStopp;
import com.example.rafael.appprototype.R;

/**
 * Created by felgueiras on 14/01/2017.
 */
public class DrugInfoStopp extends Fragment {


    public static String DRUG;
    private PrescriptionStopp drugInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drug_info_stopp, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            // get drug area
            drugInfo = (PrescriptionStopp) bundle.getSerializable(DRUG);
        }

        ListView drugIssues= (ListView) view.findViewById(R.id.issues);
        DrugIssues adapter = new DrugIssues(getActivity(), drugInfo.getIssues());
        drugIssues.setAdapter(adapter);
        return view;
    }
}
