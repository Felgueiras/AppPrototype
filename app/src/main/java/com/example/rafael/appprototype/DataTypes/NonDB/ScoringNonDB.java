package com.example.rafael.appprototype.DataTypes.NonDB;

import java.util.ArrayList;

/**
 * Created by rafael on 30-09-2016.
 */
public class ScoringNonDB  {

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
     * Correspondence between score and category.
     */
    ArrayList<GradingNonDB> values;
    /**
     * Grading particular to men
     */
    ArrayList<GradingNonDB> valuesMen;
    /**
     * Grading particular to women
     */
    ArrayList<GradingNonDB> valuesWomen;

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

    public ArrayList<GradingNonDB> getValues() {
        return values;
    }

    public void setValues(ArrayList<GradingNonDB> values) {
        this.values = values;
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
}
