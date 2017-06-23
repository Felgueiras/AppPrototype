package com.felgueiras.apps.geriatric_helper;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by felgueiras on 02/05/2017.
 */

public class PatientMetadata implements Serializable {
    @Expose
    String patientID;

    public PatientMetadata(String patientID) {
        this.patientID = patientID;
    }

    public PatientMetadata() {
        super();
    }

    public String getPatientID() {
        return patientID;
    }
}