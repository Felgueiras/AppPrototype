package com.example.rafael.appprototype.DrugPrescription.Stopp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.NonDB.GradingNonDB;
import com.example.rafael.appprototype.DataTypes.StaticTestDefinition;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Display a List of the resume for each GeriatricTest inside a Sesssion.
 */
public class DrugIssues extends BaseAdapter {
    /**
     * Questions for a Test
     */
    private final ArrayList<Issue> issues;
    private static LayoutInflater inflater = null;
    private View testView;

    /**
     * Display all Questions for a GeriatricTest
     *
     * @param issues ArrayList of Questions
     */
    public DrugIssues(Context context, ArrayList<Issue> issues) {
        this.issues = issues;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Issue issue = issues.get(position);
        // setup views
        testView = inflater.inflate(R.layout.drug_issue, parent, false);
        TextView description = (TextView) testView.findViewById(R.id.issue_description);
        TextView risk = (TextView) testView.findViewById(R.id.issue_risk);

        // update views
        description.setText(issue.getDescription());
        risk.setText(issue.getRisk());

        return testView;
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