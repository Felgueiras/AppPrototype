package com.example.rafael.appprototype.DrugPrescription;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.rafael.appprototype.DrugPrescription.StartStopp.PrescriptionStart;
import com.example.rafael.appprototype.DrugPrescription.StartStopp.StartCriterion;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StartCriteriaFragment extends Fragment {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<PrescriptionStart>> listDataChild;
    private ArrayList<StartCriterion> start;

    public StartCriteriaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StartCriteriaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StartCriteriaFragment newInstance(String param1, String param2) {
        StartCriteriaFragment fragment = new StartCriteriaFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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


    private void createStartData() {
        start = new ArrayList<>();
        // Encodrine
        StartCriterion criterion = new StartCriterion("Endocrine System");
        PrescriptionStart prescriptionStart = new PrescriptionStart("Metformin", "Metformin with type 2 diabetes +/- metabolic syndrome" +
                "(in the absence of renal impairment—estimated GFR <50ml/ min).");
        criterion.addPrescription(prescriptionStart);
        start.add(criterion);
        // Musculoskeletal
        criterion = new StartCriterion("Musculoskeletal System");
        prescriptionStart = new PrescriptionStart("Disease-modifying anti-rheumatic drug (DMARD)",
                "with active moderate-severe rheumatoid disease lasting > 12 weeks");
        criterion.addPrescription(prescriptionStart);
        start.add(criterion);
        //Gastrointestinal
        criterion = new StartCriterion("Gastrointestinal System");
        prescriptionStart = new PrescriptionStart("Proton Pump Inhibitor",
                "with severe gastro-oesophageal acid reflux disease or peptic stricture requiring dilatation.");
        criterion.addPrescription(prescriptionStart);
        prescriptionStart = new PrescriptionStart("Fibre supplement",
                "for chronic, symptomatic diverticular disease with constipation.");
        criterion.addPrescription(prescriptionStart);
        start.add(criterion);
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        for (int i = 0; i < start.size(); i++) {
            StartCriterion s = start.get(i);
            // header
            listDataHeader.add(s.getCategory());
            // child
            // Adding child data
            List<PrescriptionStart> child = new ArrayList<>();
            for (PrescriptionStart pr : s.getPrescriptions()) {
                child.add(pr);
            }
            listDataChild.put(listDataHeader.get(i), child); // Header, Child data
        }
    }
}
