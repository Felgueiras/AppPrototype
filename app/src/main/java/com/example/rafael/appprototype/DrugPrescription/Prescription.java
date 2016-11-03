package com.example.rafael.appprototype.DrugPrescription;

import java.util.ArrayList;

/**
 * Created by rafael on 01-11-2016.
 */
public class Prescription {

    String drugName;
    ArrayList<Issue> situations;

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

    public ArrayList<Issue> getSituations() {
        return situations;
    }
}
