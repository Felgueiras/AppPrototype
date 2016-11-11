package com.example.rafael.appprototype.DrugPrescription.Beers;

/**
 * Created by rafael on 03-11-2016.
 */
public class RecommendationInfo {
    private final String recommendation;
    private final String rationale;
    String qualityOfEvidence;
    String strengthOfRecommendation;

    public RecommendationInfo(String recommendation, String rationale) {
        this.recommendation = recommendation;
        this.rationale = rationale;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public String getRationale() {
        return rationale;
    }

    public String getQualityOfEvidence() {
        return qualityOfEvidence;
    }

    public void setQualityOfEvidence(String qualityOfEvidence) {
        this.qualityOfEvidence = qualityOfEvidence;
    }

    public String getStrengthOfRecommendation() {
        return strengthOfRecommendation;
    }

    public void setStrengthOfRecommendation(String strengthOfRecommendation) {
        this.strengthOfRecommendation = strengthOfRecommendation;
    }

    @Override
    public String toString() {
        return recommendation + "\n" +rationale + "\nQE: " + qualityOfEvidence + "\n" +
                "SR: " + strengthOfRecommendation;
    }
}
