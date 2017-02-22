package com.example.rafael.appprototype.DataTypes.DB;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;

/**
 * Created by rafael on 30-09-2016.
 */
@Table(name = "Gradings")
public class Grading {

    @Expose
    @Column(name = "guid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    String guid;
    /**
     * Name of the area (full dependency, mild dependency, etc)
     */
    @Expose
    @Column(name = "grade")
    String grade;
    /**
     * Score corresponding to that area. Can be a single value or a list of values.
     */
    @Expose
    @Column(name = "score")
    String score;

    /**
     * Create a new Grading area (single score)
     *
     * @param grade textual field
     * @param score numerical value for the score
     */
    public Grading(String grade, int score) {
        super();
        this.grade = grade;
        this.score = score + "";

    }

    public Grading() {
        super();
    }

    /**
     * Create a new Grading area (multiple values)
     *
     * @param grade
     * @param values
     */
    public Grading(String grade, int[] values) {
        super();
        this.grade = grade;
        this.score = values[0] + "";
        for (int i = 1; i < values.length; i++) {
            this.score += "," + values[i];
        }
    }
}
