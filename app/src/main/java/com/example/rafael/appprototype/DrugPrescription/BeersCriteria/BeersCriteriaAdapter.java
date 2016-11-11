package com.example.rafael.appprototype.DrugPrescription.BeersCriteria;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.rafael.appprototype.R;
import com.google.android.gms.common.SignInButton;

import java.util.ArrayList;


public class BeersCriteriaAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> drugs;
    private final ArrayList<BeersRecommendation> beersData;
    BeersCriteriaAdapter adapter;

    public BeersCriteriaAdapter(Context context, ArrayList<String> values, ArrayList<BeersRecommendation> beersData) {
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
     * Return the Beers criteria beersData for a certain drug name.
     *
     * @param selectedDrug
     * @return
     */
    public RecommendationInfo getBeersCriteriaInfoAboutDrug(String selectedDrug) {
        for (BeersRecommendation rec : beersData) {
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