package com.example.rafael.appprototype.DataTypes.NonDB;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rafael on 30-09-2016.
 */
public class GradingNonDB {


    /**
     * Name of the category (full dependency, mild dependency, etc)
     */
    String grade;
    /**
     * Score corresponding to that category. Can be a single value or a list of valuesBoth.
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

    /**
     * Create a new Grading category (multiple valuesBoth)
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

    /**
     * Check if the result we have for the Test corresponds to this Grading category.
     *
     * @param testResult
     * @return
     */
    public boolean containsScore(double testResult) {
        List<String> values = Arrays.asList(score.split(","));
        ArrayList<Double> vals = new ArrayList<>();
        for (String s : values) {
            vals.add(Double.parseDouble(s));
        }
        //system.out.println(testResult + "->" + vals);
        if (vals.contains(testResult))
            return true;
        return false;
    }


    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
