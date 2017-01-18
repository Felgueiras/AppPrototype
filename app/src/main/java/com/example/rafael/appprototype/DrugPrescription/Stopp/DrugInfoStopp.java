package com.example.rafael.appprototype.DrugPrescription.Stopp;

import android.app.Fragment;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.Criteria.PrescriptionStopp;
import com.example.rafael.appprototype.Evaluations.EvaluationsHistory.HistoryCard.ShowTestsForSession;
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
            // get drug name
            drugInfo = (PrescriptionStopp) bundle.getSerializable(DRUG);
        }

        ListView drugIssues= (ListView) view.findViewById(R.id.issues);
        DrugIssues adapter = new DrugIssues(getActivity(), drugInfo.getIssues());
        drugIssues.setAdapter(adapter);
        return view;
    }
}
