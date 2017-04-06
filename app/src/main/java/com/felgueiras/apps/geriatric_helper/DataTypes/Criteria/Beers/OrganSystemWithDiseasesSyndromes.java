package com.felgueiras.apps.geriatric_helper.DataTypes.Criteria.Beers;

import java.util.ArrayList;

/**
 * Created by felgueiras on 10/03/2017.
 */

public class OrganSystemWithDiseasesSyndromes {
    private final String organSystem;
    private ArrayList<DiseaseSyndrome> diseaseSyndromes;

    public OrganSystemWithDiseasesSyndromes(String organSystem) {
        this.organSystem = organSystem;
        this.diseaseSyndromes = new ArrayList<>();
    }

    public void addDiseaseSyndrome(DiseaseSyndrome diseaseSyndrome) {
        this.diseaseSyndromes.add(diseaseSyndrome);
    }

    public String getOrganSystem() {
        return organSystem;
    }

    public ArrayList<DiseaseSyndrome> getDiseaseSyndromes() {
        return diseaseSyndromes;
    }

    public void setDiseaseSyndromes(ArrayList<DiseaseSyndrome> diseaseSyndromes) {
        this.diseaseSyndromes = diseaseSyndromes;
    }
}
