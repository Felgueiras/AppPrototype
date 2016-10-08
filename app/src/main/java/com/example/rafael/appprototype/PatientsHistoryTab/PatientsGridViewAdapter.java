package com.example.rafael.appprototype.PatientsHistoryTab;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.Main.GridSpacingItemDecoration;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafael on 03-10-2016.
 */
public class PatientsGridViewAdapter extends BaseAdapter {
    private final List<Patient> patients;
    private RecyclerView recyclerView;
    private ArrayList<Patient> patientsList;
    private ViewSinglePatientCardAdapter adapter;
    Context context;

    public PatientsGridViewAdapter(Context context, List<Patient> patients) {
        this.context = context;
        this.patients = patients;
        Log.d("aba", "Grid view adapter size " + patients.size());
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // each view is a Fragment layout that holds a Fragment with a Recycler View inside
        View gridElement = inflater.inflate(R.layout.content_patients_history, null);

        Patient patient = patients.get(position);
        // fill the RecyclerView
        recyclerView = (RecyclerView) gridElement.findViewById(R.id.recycler_view);

        patientsList = new ArrayList<>();
        patientsList.add(patient);
        patientsList.add(patient);
        patientsList.add(patient);
        patientsList.add(patient);
        patientsList.add(patient);

        context = parent.getContext();
        adapter = new ViewSinglePatientCardAdapter(context, patientsList);

        // create Layout
        int numbercolumns = 3;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, numbercolumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        return gridElement;
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}