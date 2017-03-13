package com.example.rafael.appprototype.Prescription;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.Criteria.Beers.BeersCriteria;
import com.example.rafael.appprototype.DataTypes.Criteria.PrescriptionStart;
import com.example.rafael.appprototype.DataTypes.Criteria.PrescriptionStopp;
import com.example.rafael.appprototype.DataTypes.Criteria.RecommendationInfo;
import com.example.rafael.appprototype.DataTypes.Criteria.StartCriteria;
import com.example.rafael.appprototype.DataTypes.Criteria.StoppCriteria;
import com.example.rafael.appprototype.EmptyStateFragment;
import com.example.rafael.appprototype.Main.FragmentTransitions;
import com.example.rafael.appprototype.Main.PrivateArea;
import com.example.rafael.appprototype.Main.PublicArea;
import com.example.rafael.appprototype.Patients.SinglePatient.ViewSinglePatientInfo;
import com.example.rafael.appprototype.Prescription.Beers.DrugInfoBeers;
import com.example.rafael.appprototype.Prescription.Start.DrugInfoStart;
import com.example.rafael.appprototype.Prescription.Stopp.DrugInfoStopp;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

public class DrugListItem extends RecyclerView.Adapter<DrugListItem.MyViewHolder> implements Filterable {

    private final ArrayList<String> filteredList;
    private Activity context;
    /**
     * Data to be displayed.
     */
    private ArrayList<String> drugs;
    private DrugsFilter patientsFilter;
    private ArrayList<StoppCriteria> stoppData;
    private ArrayList<StartCriteria> startData;
    private ArrayList<String> stoppCriteriaDrugs;
    private ArrayList<String> beersCriteriaDrugs;
    private ArrayList<String> startCriteriaDrugs;

    @Override
    public Filter getFilter() {
        if (patientsFilter == null)
            patientsFilter = new DrugsFilter(this, drugs);
        return patientsFilter;
    }

    /**
     * Create a View
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        public Button startButton, stoppButton, beersButton;
        public TextView name;
        public ImageView photo, overflow;

        public MyViewHolder(View view) {
            super(view);
            this.view = view;
            name = (TextView) view.findViewById(R.id.patientName);
            photo = (ImageView) view.findViewById(R.id.patientPhoto);
            /**
             * Criteria buttons.
             */
            startButton = (Button) view.findViewById(R.id.startCriteria);
            stoppButton = (Button) view.findViewById(R.id.stoppCriteria);
            beersButton = (Button) view.findViewById(R.id.beersCriteria);
        }
    }

    /**
     * Constructor of the SessionCardEvaluationHistory
     *
     * @param context
     * @param patients
     */
    public DrugListItem(Activity context, ArrayList<String> patients) {
        this.context = context;
        this.drugs = patients;
        this.filteredList = new ArrayList<>();
        filteredList.addAll(patients);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drug_item, parent, false);
        // check the criterias we have for this drug
        // get drug info
        stoppData = StoppCriteria.getStoppData();
        startData = StartCriteria.getStartData();
        // stopp
        stoppCriteriaDrugs = StoppCriteria.getAllDrugsStopp(stoppData);
        // beers
        beersCriteriaDrugs = BeersCriteria.getBeersDrugsAllString();
        // start
        startCriteriaDrugs = StartCriteria.getAllDrugsStart(startData);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final String currentDrug = filteredList.get(position);

        holder.name.setText(currentDrug);

        // start
        if (startCriteriaDrugs.contains(currentDrug)) {
            holder.startButton.setVisibility(View.VISIBLE);
        }
        else{
            holder.startButton.setVisibility(View.GONE);
        }
        // stopp
        if (stoppCriteriaDrugs.contains(currentDrug)) {
            holder.stoppButton.setVisibility(View.VISIBLE);
        }else{
            holder.stoppButton.setVisibility(View.GONE);
        }
        /**
         * Beers.
         */
        if (beersCriteriaDrugs.contains(currentDrug)) {
            holder.beersButton.setVisibility(View.VISIBLE);
        }else{
            holder.beersButton.setVisibility(View.GONE);
        }


        holder.view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Fragment endFragment = new ViewSingleDrugtInfo();
                Bundle args = new Bundle();
                args.putString(ViewSingleDrugtInfo.DRUG, currentDrug);
                FragmentTransitions.replaceFragment(context, endFragment, args, Constants.tag_view_drug_info);
            }
        });


        /*
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
        */
    }


    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    /**
     * Class that allows for the Patients to be filtered by area.
     */
    private class DrugsFilter extends Filter {
        private final DrugListItem adapter;
        private final ArrayList<String> originalList;
        private final ArrayList<String> filteredList;

        public DrugsFilter(DrugListItem adapter, ArrayList<String> patients) {
            super();
            this.adapter = adapter;
            this.originalList = new ArrayList<>();
            originalList.addAll(patients);
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final String patient : originalList) {
                    if (patient.toLowerCase().trim().contains(filterPattern)) {
                        filteredList.add(patient);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            adapter.filteredList.clear();
            adapter.filteredList.addAll((ArrayList<String>) filterResults.values);
            adapter.notifyDataSetChanged();
        }
    }
}
