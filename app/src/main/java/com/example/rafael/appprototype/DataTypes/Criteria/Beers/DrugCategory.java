package com.example.rafael.appprototype.DataTypes.Criteria.Beers;

import java.util.ArrayList;

/**
 * Created by felgueiras on 10/03/2017.
 */

public class DrugCategory {
    private final String category;
    private ArrayList<String> drugs;

    public DrugCategory(String category) {
        this.category = category;
        this.drugs = new ArrayList<String>();
    }


    public void addDrug(String drug) {
        drugs.add(drug);
    }

    public String getCategory() {
        return category;
    }

    public ArrayList<String> getDrugs() {
        return drugs;
    }

    public void setDrugs(ArrayList<String> drugs) {
        this.drugs = drugs;
    }
}
