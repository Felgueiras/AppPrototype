package com.felgueiras.apps.geriatrichelper.DataTypes.Criteria.Beers;

import com.felgueiras.apps.geriatrichelper.DataTypes.Criteria.RecommendationInfo;

import java.util.ArrayList;

/**
 * Created by felgueiras on 10/03/2017.
 */

public class DiseaseSyndrome {
    private final String diseaseSyndrome;
    private RecommendationInfo recommendationInfo;
    private ArrayList<DrugCategory> drugCategories;

    public DiseaseSyndrome(String diseaseSynrome) {
        this.diseaseSyndrome = diseaseSynrome;
        this.drugCategories = new ArrayList<>();
    }


    public void setRecommendationInfo(RecommendationInfo recommendationInfo) {
        this.recommendationInfo = recommendationInfo;
    }

    public RecommendationInfo getRecommendationInfo() {
        return recommendationInfo;
    }

    public void addDrugCategory(DrugCategory category) {
        this.drugCategories.add(category);
    }

    public String getDiseaseSyndrome() {
        return diseaseSyndrome;
    }

    public ArrayList<DrugCategory> getDrugCategories() {
        return drugCategories;
    }

    public void setDrugCategories(ArrayList<DrugCategory> drugCategories) {
        this.drugCategories = drugCategories;
    }

    public String getDrugsAsList() {
        String ret = "";
        for (DrugCategory category : drugCategories) {
            // category questionTextView
            ret += category.getCategory() + "\n";
            for (String drug : category.getDrugs()) {
                ret += "• " + drug + "\n";
            }

        }
        return ret;
    }
}
