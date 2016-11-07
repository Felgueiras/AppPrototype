package com.example.rafael.appprototype.DrugPrescription.StartStopp;

import java.util.ArrayList;

/**
 * Created by rafael on 01-11-2016.
 */
public class Prescription {

    String drugName;
    ArrayList<Issue> situations;
    private String issuesText;

    public Prescription(String name) {
        this.drugName = name;
        this.situations = new ArrayList<>();
    }

    public void addIssue(Issue issue) {
        situations.add(issue);
    }

    public String getDrugName() {
        return drugName;
    }

    public ArrayList<Issue> getIssues() {
        return situations;
    }

    public String getIssuesText() {
        String ret = "";
        for (Issue issue : situations) {
            ret += issue.toString();
        }
        return ret;
    }
}
