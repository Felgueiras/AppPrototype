package com.example.rafael.appprototype.DrugPrescription.Start;

import com.example.rafael.appprototype.DrugPrescription.PrescriptionGeneral;

/**
 * Created by rafael on 01-11-2016.
 */
public class PrescriptionStart extends PrescriptionGeneral{

    String description;

    public PrescriptionStart(String drugName, String description) {
        super(drugName);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return super.getDrugName() + ": " + description;
    }
}
