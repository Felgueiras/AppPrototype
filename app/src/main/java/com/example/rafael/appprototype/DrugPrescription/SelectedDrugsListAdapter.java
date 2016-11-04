package com.example.rafael.appprototype.DrugPrescription;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.rafael.appprototype.DrugPrescription.StartStopp.Issue;
import com.example.rafael.appprototype.DrugPrescription.StartStopp.StoppGeneral;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;


public class SelectedDrugsListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> values;
    private final StoppGeneral stoppGeneral;

    public SelectedDrugsListAdapter(Context context, ArrayList<String> values, StoppGeneral stoppGeneral) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.stoppGeneral = stoppGeneral;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String selectedDrug = values.get(position);
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.selected_drug, parent, false);
        TextView drugName = (TextView) rowView.findViewById(R.id.drug_name);
        ImageView colorCode = (ImageView) rowView.findViewById(R.id.color_code);
        colorCode.setBackgroundColor(Color.RED);
        drugName.setText(selectedDrug);
        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
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
                ArrayList<Issue> issuesForGivenDrug = stoppGeneral.getIssuesForGivenDrug(selectedDrug);
                TextView issueDescription = (TextView) drugInfo.findViewById(R.id.description);
                Issue currentIssue = issuesForGivenDrug.get(0);
                issueDescription.setText(currentIssue.getDescription());
                final PopupWindow popUp = new PopupWindow(drugInfo, 500, 500, false);
                popUp.setTouchable(false);
                popUp.setFocusable(false);
                popUp.setOutsideTouchable(true);
                popUp.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1]+200);
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
        return rowView;
    }


}