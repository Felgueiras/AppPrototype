package com.example.rafael.appprototype.DrugPrescription.StartStopp;

import java.util.ArrayList;

/**
 * Information about a StartCriterion for a certain category.
 */
public class StartCriterion {

    String category;
    ArrayList<PrescriptionStart> prescriptions;

    public StartCriterion(String category) {
        this.category = category;
        this.prescriptions = new ArrayList<>();
    }

    public void addPrescription(PrescriptionStart prescriptionStart) {
        prescriptions.add(prescriptionStart);
    }

    public String getCategory() {
        return category;
    }

    public ArrayList<PrescriptionStart> getPrescriptions() {
        return prescriptions;
    }
}
