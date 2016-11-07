package com.example.rafael.appprototype.DrugPrescription.BeersCriteria;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.rafael.appprototype.DrugPrescription.StartStopp.Issue;
import com.example.rafael.appprototype.DrugPrescription.StartStopp.Prescription;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;


public class BeersCriteriaAdapter extends ArrayAdapter<RecommendationInfo> {
    private final Context context;
    private final ArrayList<RecommendationInfo> values;
    private final BeersRecommendation beersRecommendations;
    BeersCriteriaAdapter adapter;

    public BeersCriteriaAdapter(Context context, ArrayList<RecommendationInfo> values, BeersRecommendation beersRecommendations) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.beersRecommendations = beersRecommendations;
        adapter = this;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Prescription selectedDrug = values.get(position);
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.selected_drug, parent, false);
        TextView drugName = (TextView) rowView.findViewById(R.id.drug_name);
        TextView drugDescription = (TextView) rowView.findViewById(R.id.drugDescription);
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
         * Drug was clicked.
         */

        rowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                System.out.println("Clicked something - " + selectedDrug);
                /**
                 * Show PopupWindow with information about that drug
                 */
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                View drugInfo = inflater.inflate(R.layout.drug_info, null, false);
                /**
                 * Fill the popup view
                 */
                // get all the issues related to that drug (display only one issue)
                ArrayList<Issue> issuesForGivenDrug = stoppGeneral.getIssuesForGivenDrug(selectedDrug.getDrugName());
                TextView issueDescription = (TextView) drugInfo.findViewById(R.id.description);
                Issue currentIssue = issuesForGivenDrug.get(0);
                issueDescription.setText(currentIssue.getDescription());
                final PopupWindow popUp = new PopupWindow(drugInfo, 500, 500, false);
                popUp.setTouchable(false);
                popUp.setFocusable(false);
                popUp.setOutsideTouchable(true);
                popUp.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] + 200);
                Handler handler = new Handler();
                Runnable r = new Runnable() {
                    public void run() {
                        popUp.dismiss();
                    }
                };
                handler.postDelayed(r, 1000);
                return false;
            }
        });
        /**
         * Long press - remove from list.
         */
        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                System.out.println("Long press");
                values.remove(0);
                // update gui
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        return rowView;
    }


}