package com.example.rafael.appprototype.DrugPrescription.Beers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.rafael.appprototype.R;

import java.util.ArrayList;


public class BeersCriteriaAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> drugs;
    private final ArrayList<BeersCriteria> beersData;
    BeersCriteriaAdapter adapter;

    public BeersCriteriaAdapter(Context context, ArrayList<String> values, ArrayList<BeersCriteria> beersData) {
        super(context, -1, values);
        this.context = context;
        this.drugs = values;
        this.beersData = beersData;
        adapter = this;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // get RecommendationInfo for that drug
        String drugName = drugs.get(position);
        RecommendationInfo selectedDrug = getBeersCriteriaInfoAboutDrug(drugName);
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.selected_drug, parent, false);
        TextView drugNameTextView = (TextView) rowView.findViewById(R.id.drug_name);
        TextView drugDescription = (TextView) rowView.findViewById(R.id.drugDescription);
        drugNameTextView.setText(drugName);
        drugDescription.setText(selectedDrug.toString());

        Button closeButton = (Button) rowView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Long press");
                drugs.remove(position);
                // update gui
                adapter.notifyDataSetChanged();
            }
        });


        return rowView;
    }

    /**
     * Return the Beers criteria beersGeneral for a certain drug patientName.
     *
     * @param selectedDrug
     * @return
     */
    public RecommendationInfo getBeersCriteriaInfoAboutDrug(String selectedDrug) {
        for (BeersCriteria rec : beersData) {
            for (TherapeuticCategoryEntry entry : rec.getEntries()) {
                ArrayList<String> drugs = entry.getDrugs();
                if (drugs.contains(selectedDrug)) {
                    return entry.getInfo();
                }
            }
        }
        return null;
    }


}