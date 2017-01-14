package com.example.rafael.appprototype.DataTypes.Criteria;

import java.io.Serializable;

/**
 * Created by rafael on 03-11-2016.
 */
public class RecommendationInfo extends PrescriptionGeneral implements Serializable {
    private final String recommendation;
    private final String rationale;
    String qualityOfEvidence;
    String strengthOfRecommendation;

    public RecommendationInfo(String recommendation, String rationale) {
        super(null);
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

    /**
     * Return the textual description of all the data for a recommendation.
     *
     * @return
     */
    public String getDescription() {
        String ret = "";
        ret += "Recommendation: " +recommendation + "\n";
        ret += "Rationale: " + rationale + "\n";
        ret += "QE: " + qualityOfEvidence + "\n";
        ret += "SR: " + strengthOfRecommendation + "\n";
        return ret;
    }


    @Override
    public String toString() {
        return recommendation + "\n" + rationale + "\nQE: " + qualityOfEvidence + "\n" +
                "SR: " + strengthOfRecommendation;
    }
}
