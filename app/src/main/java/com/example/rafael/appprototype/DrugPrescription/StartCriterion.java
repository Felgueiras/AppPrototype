package com.example.rafael.appprototype.DrugPrescription;

import java.util.ArrayList;

/**
 * Created by rafael on 01-11-2016.
 */

public class StartCriterion {

    String category;
    ArrayList<PrescriptionStart> prescriptions;

    public StartCriterion(String category) {
        this.category = category;
        prescriptions = new ArrayList<>();
    }

    public void addPrescription(PrescriptionStart prescriptionStart) {
        prescriptions.add(prescriptionStart);
    }
}
