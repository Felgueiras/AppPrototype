package com.example.rafael.appprototype.DrugPrescription.StartStopp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.rafael.appprototype.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StartCriteriaFragment extends Fragment {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<PrescriptionStart>> listDataChild;
    private ArrayList<StartCriterion> startGeneral;

    public StartCriteriaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_start_criteria, container, false);

        // get the listview
        expListView = (ExpandableListView) v.findViewById(R.id.lvExp);

        // preparing list data
        createStartData();
        prepareListData();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getActivity(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getActivity(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                Toast.makeText(getActivity(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
        // Inflate the layout for this fragment
        return v;
    }


    /**
     * Create startGeneral criteria.
     */
    private void createStartData() {
        startGeneral = new ArrayList<>();
        // Encodrine
        StartCriterion criterion = new StartCriterion("Endocrine System");
        PrescriptionStart prescriptionStart = new PrescriptionStart("Metformin", "Metformin with type 2 diabetes +/- metabolic syndrome" +
                "(in the absence of renal impairment—estimated GFR <50ml/ min).");
        criterion.addPrescription(prescriptionStart);
        startGeneral.add(criterion);
        // Musculoskeletal
        criterion = new StartCriterion("Musculoskeletal System");
        prescriptionStart = new PrescriptionStart("Disease-modifying anti-rheumatic drug (DMARD)",
                "with active moderate-severe rheumatoid disease lasting > 12 weeks");
        criterion.addPrescription(prescriptionStart);
        startGeneral.add(criterion);
        //Gastrointestinal
        criterion = new StartCriterion("Gastrointestinal System");
        prescriptionStart = new PrescriptionStart("Proton Pump Inhibitor",
                "with severe gastro-oesophageal acid reflux disease or peptic stricture requiring dilatation.");
        criterion.addPrescription(prescriptionStart);
        prescriptionStart = new PrescriptionStart("Fibre supplement",
                "for chronic, symptomatic diverticular disease with constipation.");
        criterion.addPrescription(prescriptionStart);
        startGeneral.add(criterion);
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        for (int i = 0; i < startGeneral.size(); i++) {
            StartCriterion s = startGeneral.get(i);
            // header
            listDataHeader.add(s.getCategory());
            // child
            List<PrescriptionStart> child = new ArrayList<>();
            for (PrescriptionStart pr : s.getPrescriptions()) {
                child.add(pr);
            }
            listDataChild.put(listDataHeader.get(i), child); // Header, Child data
        }
    }
}
