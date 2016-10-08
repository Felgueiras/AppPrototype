package com.example.rafael.appprototype.DataTypes.DB;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by rafael on 30-09-2016.
 */
@Table(name = "Scorings")
public class Scoring extends Model {

    @Column(name = "guid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    String guid;
    /**
     * Min score for a given test
     */
    @Column(name = "minScore")
    int minScore;
    /**
     * Max score
     */
    @Column(name = "maxScore")
    int maxScore;
    /**
     * Boolean that indicates if the result can vary from men to women
     */
    @Column(name = "differentMenWomen")
    boolean differentMenWomen;
    /**
     * Correspondence between score and category.
     */
    @Column(name = "values")
    Grading[] values;
    /**
     * Grading particular to men
     */
    @Column(name = "valuesMen")
    Grading[] valuesMen;
    /**
     * Grading particular to women
     */
    @Column(name = "testName")
    Grading[] valuesWomen;

    /**
     * Define a new Scoring for a Test
     *
     * @param minScore          min score
     * @param maxScore          max score
     * @param differentMenWomen different scores for men and women
     */
    public Scoring(int minScore, int maxScore, boolean differentMenWomen) {
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

    public Grading[] getValues() {
        return values;
    }

    public void setValues(Grading[] values) {
        this.values = values;
    }

    public Grading[] getValuesMen() {
        return valuesMen;
    }

    public void setValuesMen(Grading[] valuesMen) {
        this.valuesMen = valuesMen;
    }

    public Grading[] getValuesWomen() {
        return valuesWomen;
    }

    public void setValuesWomen(Grading[] valuesWomen) {
        this.valuesWomen = valuesWomen;
    }
}
