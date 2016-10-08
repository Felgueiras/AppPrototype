package com.example.rafael.appprototype.DataTypes.DB;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by rafael on 30-09-2016.
 */
@Table(name = "Choices")
public class Choice extends Model {
    @Column(name = "guid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    String guid;
    /**
     * Name of the choice.
     */
    @Column(name = "name")
    String name;
    /**
     * Description of the choice.
     */
    @Column(name = "description")
    String description;
    /**
     * Score for that choice for that question;
     */
    @Column(name = "score")
    int score;
    /**
     * Score if yes answer.
     */
    @Column(name = "yes")
    int yes;
    /**
     * Score if no question.
     */
    @Column(name = "no")
    int no;

    /**
     * Question for this Choice
     */
    @Column(name = "question")
    Question question;

    /**
     * Create a new Choice for a Question
     *
     * @param name        date of the choice
     * @param description description
     * @param score       score for that Choice
     */
    public Choice(String name, String description, int score) {
        super();
        this.name = name;
        this.description = description;
        this.score = score;
    }

    public Choice(String description, int score) {
        super();
        this.description = description;
        this.score = score;
    }

    public Choice(int yes, int no) {
        super();
        this.yes = yes;
        this.no = no;
    }

    public Choice() {
        super();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getYes() {
        return yes;
    }

    public void setYes(int yes) {
        this.yes = yes;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}
