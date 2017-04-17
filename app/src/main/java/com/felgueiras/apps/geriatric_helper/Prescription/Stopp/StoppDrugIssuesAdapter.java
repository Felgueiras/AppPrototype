package com.felgueiras.apps.geriatric_helper.Prescription.Stopp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.R;

import java.util.ArrayList;

/**
 * Display a List of the resume for each GeriatricScale inside a Sesssion.
 */
public class StoppDrugIssuesAdapter extends RecyclerView.Adapter<StoppDrugIssuesAdapter.DrugIssue> {
    /**
     * Questions for a Test
     */
    private final ArrayList<Issue> issues;
    private static LayoutInflater inflater = null;
    private View view;

    /**
     * Display all Questions for a GeriatricScale
     *
     * @param issues ArrayList of Questions
     */
    public StoppDrugIssuesAdapter(Context context, ArrayList<Issue> issues) {
        this.issues = issues;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class DrugIssue extends RecyclerView.ViewHolder {
        private final TextView description,risk;

        public DrugIssue(View view) {
            super(view);
            // get view
            description = (TextView) view.findViewById(R.id.issue_description);
            risk = (TextView) view.findViewById(R.id.issue_risk);
        }
    }


    @Override
    public StoppDrugIssuesAdapter.DrugIssue onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.stopp_drug_issue, parent, false);
        return new StoppDrugIssuesAdapter.DrugIssue(itemView);
    }

    @Override
    public void onBindViewHolder(DrugIssue holder, int position) {
        Issue issue = issues.get(position);

        // update views
        holder.description.setText(issue.getDescription());
        holder.risk.setText(issue.getRisk());
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return issues.size();
    }


}