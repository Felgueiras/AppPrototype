package com.example.rafael.appprototype.Patients.ViewPatients;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.Patients.ViewPatients.SinglePatient.CreatePatientCard;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

/**
 * Created by rafael on 03-10-2016.
 */
public class CreatePatientsList extends BaseAdapter {
    /**
     * All the Patients
     */
    private ArrayList<Patient> patients;
    Context context;

    public CreatePatientsList(Context context, ArrayList<Patient> patients) {
        this.context = context;
        this.patients = patients;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        context = parent.getContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // each view is a Fragment layout that holds a Fragment with a Recycler View inside
        View gridElement = inflater.inflate(R.layout.content_patients_list, null);

        // fill the RecyclerView
        RecyclerView recyclerView = (RecyclerView) gridElement.findViewById(R.id.recycler_view);

        // create Layout
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        CreatePatientCard adapter = new CreatePatientCard(context, patients);
        recyclerView.setAdapter(adapter);

        return gridElement;
    }


    @Override
    public int getCount() {
        return 1;
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