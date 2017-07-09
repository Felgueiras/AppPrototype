package com.felgueiras.apps.geriatric_helper.DataTypes.NonDB;

import com.activeandroid.util.Log;
import com.felgueiras.apps.geriatric_helper.Constants;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rafael on 30-09-2016.
 */
public class ScoringNonDB implements Serializable{

    /**
     * Min score for a given test
     */
    int minScore;
    /**
     * Max score
     */
    int maxScore;
    /**
     * Boolean that indicates if the result can vary from men to women
     */
    boolean differentMenWomen;
    /**
     * Correspondence between score and area.
     */
    ArrayList<GradingNonDB> valuesBoth;
    /**
     * Grading particular to men
     */
    ArrayList<GradingNonDB> valuesMen;
    /**
     * Grading particular to women
     */
    ArrayList<GradingNonDB> valuesWomen;
    private int minMen;
    private int maxMen;

    String name;

    /**
     * Define a new Scoring for a Test
     *
     * @param minScore          min score
     * @param maxScore          max score
     * @param differentMenWomen different scores for men and women
     */
    public ScoringNonDB(int minScore, int maxScore, boolean differentMenWomen) {
        this.minScore = minScore;
        this.maxScore = maxScore;
        this.differentMenWomen = differentMenWomen;
    }


    public int getMinScore() {
        return minScore;
    }

    public void setMinScore(int minScore) {
        this.minScore = minScore;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public boolean isDifferentMenWomen() {
        return differentMenWomen;
    }

    public void setDifferentMenWomen(boolean differentMenWomen) {
        this.differentMenWomen = differentMenWomen;
    }

    public ArrayList<GradingNonDB> getValuesBoth() {
        return valuesBoth;
    }

    public void setValuesBoth(ArrayList<GradingNonDB> valuesBoth) {
        this.valuesBoth = valuesBoth;
    }

    public ArrayList<GradingNonDB> getValuesMen() {
        return valuesMen;
    }

    public void setValuesMen(ArrayList<GradingNonDB> valuesMen) {
        this.valuesMen = valuesMen;
    }

    public ArrayList<GradingNonDB> getValuesWomen() {
        return valuesWomen;
    }

    public void setValuesWomen(ArrayList<GradingNonDB> valuesWomen) {
        this.valuesWomen = valuesWomen;
    }

    /**
     * Get a Grading by the test result.
     *
     * @param testResult
     * @param gender
     * @return
     */
    public GradingNonDB getGrading(double testResult, int gender) {
        GradingNonDB match = null;
        ArrayList<GradingNonDB> toConsider = null;
        if (gender == Constants.MALE)
            toConsider = valuesMen;
        else if (gender == Constants.FEMALE)
            toConsider = valuesWomen;
        else
            toConsider = valuesBoth;
        for (GradingNonDB grading : toConsider) {
            // check the grading for the result we have
            if (grading.containsScore(testResult)) {
                match = grading;
                break;
            }
        }
        return match;
    }

    public int getGradingIndex(double testResult, int gender) {
        Log.d("Gender", gender + "");
        ArrayList<GradingNonDB> toConsider = null;
        if (differentMenWomen) {
            if (gender == Constants.MALE)
                toConsider = valuesMen;
            else if (gender == Constants.FEMALE)
                toConsider = valuesWomen;
        } else {
            toConsider = valuesBoth;
        }


        for (int i = 0; i < toConsider.size(); i++) {
            GradingNonDB grading = toConsider.get(i);
            // check the grading for the result we have
            if (grading.containsScore(testResult)) {
                return i;
            }
        }
        return -1;
    }


    public void setScoringMen(int min, int max) {
        this.minMen = min;
        this.maxMen = max;
    }

    public int getMinMen() {
        return minMen;
    }

    public void setMinMen(int minMen) {
        this.minMen = minMen;
    }

    public int getMaxMen() {
        return maxMen;
    }

    public void setMaxMen(int maxMen) {
        this.maxMen = maxMen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
