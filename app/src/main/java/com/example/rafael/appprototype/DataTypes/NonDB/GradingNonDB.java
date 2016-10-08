package com.example.rafael.appprototype.DataTypes.NonDB;


/**
 * Created by rafael on 30-09-2016.
 */
public class GradingNonDB {


    /**
     * Name of the category (full dependency, mild dependency, etc)
     */
    String grade;
    /**
     * Score corresponding to that category. Can be a single value or a list of values.
     */
    String score;
    /**
     * More detailed description of the grading
     */
    String description;

    /**
     * Create a new Grading category (single score)
     *
     * @param grade textual description
     * @param score numerical value for the score
     */
    public GradingNonDB(String grade, int score) {
        this.grade = grade;
        this.score = score + "";
    }

    public GradingNonDB(String grade, int score, String description) {
        this.grade = grade;
        this.score = score + "";
        this.description = description;
    }


    public GradingNonDB() {

    }

    /**
     * Create a new Grading category (multiple values)
     *
     * @param grade
     * @param values
     */
    public GradingNonDB(String grade, int[] values) {
        super();
        this.grade = grade;
        this.score = values[0] + "";
        for (int i = 1; i < values.length; i++) {
            this.score += "," + values[i];
        }
    }
}
