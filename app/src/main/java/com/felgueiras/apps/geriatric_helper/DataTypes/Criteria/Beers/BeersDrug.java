package com.felgueiras.apps.geriatric_helper.DataTypes.Criteria.Beers;

import com.felgueiras.apps.geriatric_helper.DataTypes.Criteria.RecommendationInfo;

/**
 * Created by felgueiras on 11/03/2017.
 */

public class BeersDrug {
    private final String drug;
    private RecommendationInfo recommendationInfo;

    public BeersDrug(String drug) {
        this.drug = drug;
    }

    public void setRecommendationInfo(RecommendationInfo recommendationInfo) {
        this.recommendationInfo = recommendationInfo;
    }

    public RecommendationInfo getRecommendationInfo() {
        return recommendationInfo;
    }

    public String getDrug() {
        return drug;
    }
}
