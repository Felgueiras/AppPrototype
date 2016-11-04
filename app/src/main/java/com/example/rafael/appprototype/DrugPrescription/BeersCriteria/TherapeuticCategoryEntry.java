package com.example.rafael.appprototype.DrugPrescription.BeersCriteria;

import java.util.ArrayList;

/**
 * Created by rafael on 01-11-2016.
 */
public class TherapeuticCategoryEntry {

    String categoryName;
    ArrayList<String> drugs;
    RecommendationInfo info;

    public TherapeuticCategoryEntry(String category) {
        this.categoryName = category;

    }

    public void setDrugs(ArrayList<String> drugs) {
        this.drugs = drugs;
    }


    public ArrayList<String> getDrugs() {
        return drugs;
    }


    public RecommendationInfo getInfo() {
        return info;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setInfo(RecommendationInfo info) {
        this.info = info;
    }
}