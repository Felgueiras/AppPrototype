package com.example.rafael.appprototype.DrugPrescription;

import java.util.ArrayList;

/**
 * Created by rafael on 01-11-2016.
 */

public class StoppCriterion {

    String category;
    ArrayList<Prescription> prescriptions;

    public StoppCriterion(String category) {
        this.category = category;
        prescriptions = new ArrayList<>();

    }

    public void addPrescription(Prescription pr) {
        prescriptions.add(pr);
    }

    public String getCategory() {
        return category;
    }

    public ArrayList<Prescription> getPrescriptions() {
        return prescriptions;
    }
}
