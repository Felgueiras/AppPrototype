package com.example.rafael.appprototype.DataTypes.Criteria;

import com.example.rafael.appprototype.Prescription.Stopp.Issue;

import java.io.Serializable;
import java.util.ArrayList;


public class PrescriptionStopp extends PrescriptionGeneral implements Serializable {

    ArrayList<Issue> situations;

    public PrescriptionStopp(String drugName) {
        super(drugName);
        this.situations = new ArrayList<>();
    }

    public void addIssue(Issue issue) {
        situations.add(issue);
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

    @Override
    public String toString() {
        return super.getDrugName() + ": " + getIssuesText();
    }
}
