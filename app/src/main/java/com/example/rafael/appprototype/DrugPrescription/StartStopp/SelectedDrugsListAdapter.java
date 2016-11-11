package com.example.rafael.appprototype.DrugPrescription.StartStopp;

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

import java.util.ArrayList;

/**
 * Adapter to display the drugs selected from the search.
 */
public class SelectedDrugsListAdapter extends ArrayAdapter<Prescription> {
    private final Context context;
    private final ArrayList<Prescription> values;
    private final StoppGeneral stoppGeneral;
    SelectedDrugsListAdapter adapter;

    public SelectedDrugsListAdapter(Context context, ArrayList<Prescription> values, StoppGeneral stoppGeneral) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.stoppGeneral = stoppGeneral;
        adapter = this;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Prescription selectedDrug = values.get(position);
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.selected_drug, parent, false);
        TextView drugName = (TextView) rowView.findViewById(R.id.drug_name);
        TextView drugDescription = (TextView) rowView.findViewById(R.id.drugDescription);
        Button closeButton = (Button) rowView.findViewById(R.id.close_button);
        //ImageView colorCode = (ImageView) rowView.findViewById(R.id.color_code);
        //colorCode.setBackgroundColor(Color.RED);
        drugName.setText(selectedDrug.getDrugName());
        drugDescription.setText(selectedDrug.getIssuesText());

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