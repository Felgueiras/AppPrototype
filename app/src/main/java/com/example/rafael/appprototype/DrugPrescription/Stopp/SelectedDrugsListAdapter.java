package com.example.rafael.appprototype.DrugPrescription.Stopp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rafael.appprototype.DrugPrescription.Beers.RecommendationInfo;
import com.example.rafael.appprototype.DrugPrescription.PrescriptionGeneral;
import com.example.rafael.appprototype.DrugPrescription.Start.PrescriptionStart;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

/**
 * Adapter to display the drugs selected from the search.
 */
public class SelectedDrugsListAdapter extends ArrayAdapter<PrescriptionGeneral> {
    private final Context context;
    private final ArrayList<PrescriptionGeneral> values;
    SelectedDrugsListAdapter adapter;
    LayoutInflater inflater;

    public SelectedDrugsListAdapter(Context context, ArrayList<PrescriptionGeneral> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        adapter = this;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.selected_drug, parent, false);

        // get current drug
        final PrescriptionGeneral selectedDrug = values.get(position);

        // setup views
        TextView drugName = (TextView) rowView.findViewById(R.id.drug_name);
        TextView drugDescription = (TextView) rowView.findViewById(R.id.drugDescription);
        Button closeButton = (Button) rowView.findViewById(R.id.close_button);
        // tell if it's Start or Stopp criteria
        ImageView colorCode = (ImageView) rowView.findViewById(R.id.colorCode);
        drugName.setText(selectedDrug.getDrugName());

        String className = selectedDrug.getClass().getName();

        if (className.equals(PrescriptionStopp.class.getName())) {
            // stopp
            drugDescription.setText(((PrescriptionStopp) selectedDrug).getIssuesText());
            colorCode.setBackgroundColor(Color.RED);

        } else if (className.equals(PrescriptionStart.class.getName())) {
            // start
            drugDescription.setText(((PrescriptionStart) selectedDrug).getDescription());
            colorCode.setBackgroundColor(Color.GREEN);
        } else if(className.equals(RecommendationInfo.class.getName()))
        {
            // beers
            drugDescription.setText(((RecommendationInfo) selectedDrug).getDescription());
            colorCode.setBackgroundColor(Color.BLUE);
        }


        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });

        /**
         * Long press - remove from list.
         */
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Long press");
                values.remove(position);
                // update gui
                adapter.notifyDataSetChanged();
            }
        });

        return rowView;
    }


}