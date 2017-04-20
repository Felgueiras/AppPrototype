package com.felgueiras.apps.geriatric_helper.Prescription.Start;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.felgueiras.apps.geriatric_helper.DataTypes.Criteria.PrescriptionStart;
import com.felgueiras.apps.geriatric_helper.DataTypes.Criteria.StartCriteria;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseStorageHelper;
import com.felgueiras.apps.geriatric_helper.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PrescriptionStartCriteriaFragment extends Fragment {

    ExpandableListAdapterStart listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<PrescriptionStart>> listDataChild;
    private ArrayList<StartCriteria> startGeneral;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_info, menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Prescrição");
                alertDialog.setMessage(getActivity().getResources().getString(R.string.info_prescription));
                alertDialog.show();
        }
        return true;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_start_criteria, container, false);

        // get the listview
        expListView = (ExpandableListView) v.findViewById(R.id.lvExp);

        // preparing list data
        startGeneral = FirebaseStorageHelper.getStartCriteria();
        prepareListData();

        listAdapter = new ExpandableListAdapterStart(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {


            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                return false;
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        for (int i = 0; i < startGeneral.size(); i++) {
            StartCriteria s = startGeneral.get(i);
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
