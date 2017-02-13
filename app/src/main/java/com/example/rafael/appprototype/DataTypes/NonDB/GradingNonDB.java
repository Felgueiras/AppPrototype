package com.example.rafael.appprototype.DataTypes.NonDB;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rafael on 30-09-2016.
 */
public class GradingNonDB {


    /**
     * Name of the area (full dependency, mild dependency, etc)
     */
    String grade;
    /**
     * Score corresponding to that area. Can be a single value or a list of valuesBoth.
     */
    String score;
    /**
     * More detailed field of the grading
     */
    String description;

    /**
     * Create a new Grading area (single score)
     *
     * @param grade textual field
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
     * Create a new Grading area (multiple valuesBoth)
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
     * Create a GRading with a (min, max) range.
     *
     * @param grade
     * @param min
     * @param max
     */
    public GradingNonDB(String grade, int min, int max) {
        super();
        this.grade = grade;
        this.score = min + "";
        for (int i = min + 1; i <= max; i++) {
            this.score += "," + i;
        }
    }

    /**
     * Check if the result we have for the Test corresponds to this Grading area.
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
