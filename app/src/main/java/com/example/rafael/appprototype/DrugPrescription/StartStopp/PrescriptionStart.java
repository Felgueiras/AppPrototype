package com.example.rafael.appprototype.DrugPrescription.StartStopp;

/**
 * Created by rafael on 01-11-2016.
 */
public class PrescriptionStart {

    String drugName;
    String description;

    public PrescriptionStart(String name, String description) {
        this.drugName = name;
        this.description = description;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return drugName + ": " + description;
    }
}
