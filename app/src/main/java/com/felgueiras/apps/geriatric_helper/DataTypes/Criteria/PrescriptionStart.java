package com.felgueiras.apps.geriatric_helper.DataTypes.Criteria;

import java.io.Serializable;

/**
 * Created by rafael on 01-11-2016.
 */
public class PrescriptionStart extends PrescriptionGeneral implements Serializable {

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
