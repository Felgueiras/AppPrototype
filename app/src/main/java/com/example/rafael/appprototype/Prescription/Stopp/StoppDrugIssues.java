package com.example.rafael.appprototype.Prescription.Stopp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rafael.appprototype.R;

import java.util.ArrayList;

/**
 * Display a List of the resume for each GeriatricScale inside a Sesssion.
 */
public class StoppDrugIssues extends BaseAdapter {
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
    public StoppDrugIssues(Context context, ArrayList<Issue> issues) {
        this.issues = issues;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Issue issue = issues.get(position);
        // setup main view
        view = inflater.inflate(R.layout.drug_issue, parent, false);

        // get view
        TextView description = (TextView) view.findViewById(R.id.issue_description);
        TextView risk = (TextView) view.findViewById(R.id.issue_risk);

        // update views
        description.setText(issue.getDescription());
        risk.setText(issue.getRisk());

        return view;
    }

    @Override
    public int getCount() {
        return issues.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}