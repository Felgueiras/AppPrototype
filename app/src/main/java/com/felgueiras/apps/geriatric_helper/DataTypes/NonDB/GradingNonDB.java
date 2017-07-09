package com.felgueiras.apps.geriatric_helper.DataTypes.NonDB;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rafael on 30-09-2016.
 */
public class GradingNonDB implements Serializable {


    private int min = -1;
    private int max = -1;
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
        this.min = score;
    }

    public GradingNonDB(String grade, int score, String description) {
        this.grade = grade;
        this.score = score + "";
        this.min = score;
        this.description = description;
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
        this.min = min;
        this.max = max;
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
//        List<String> values = Arrays.asList(score.split(","));
//        ArrayList<Double> vals = new ArrayList<>();
//        for (String s : values) {
//            vals.add(Double.parseDouble(s));
//        }
//        return vals.contains(testResult);

        return (testResult >= min && testResult <= max);

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

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
