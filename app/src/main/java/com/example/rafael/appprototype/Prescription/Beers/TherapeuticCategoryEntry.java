package com.example.rafael.appprototype.Prescription.Beers;

import com.example.rafael.appprototype.DataTypes.Criteria.RecommendationInfo;

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

    public String getCategoryName() {
        return categoryName;
    }

    public void setDrugs(ArrayList<String> drugs) {
        this.drugs = drugs;
    }


    public ArrayList<String> getDrugs() {
        return drugs;
    }

    public String getDrugsAsList() {
        String ret = "";
        for (String drug : drugs) {
            ret += "• " + drug + "\n";
        }
        return ret;
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

    @Override
    public String toString() {
        return info.toString();
    }
}
