package com.felgueiras.apps.geriatrichelper.DataTypes.DB;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafael on 30-09-2016.
 */
@Table(name = "Choices")
public class Choice extends Model {
    @Expose
    @Column(name = "guid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    String guid;
    /**
     * Name of the choice.
     */
    @Expose
    @Column(name = "patientName")
    String name;
    /**
     * Description of the choice.
     */
    @Expose
    @Column(name = "field")
    String description;
    /**
     * Score for that choice for that question;
     */
    @Expose
    @Column(name = "score")
    double score;
    /**
     * Score if yes answer.
     */
    @Expose
    @Column(name = "yes")
    int yes;
    /**
     * Score if no question.
     */
    @Expose
    @Column(name = "no")
    int no;

    /**
     * Question for this Choice
     */
    @Expose
    @Column(name = "question", onDelete = Column.ForeignKeyAction.CASCADE)
    Question question;

    /**
     * Create a new Choice for a Question
     *
     * @param name        date of the choice
     * @param description field
     * @param score       score for that Choice
     */
    public Choice(String name, String description, int score) {
        super();
        this.name = name;
        this.description = description;
        this.score = score;
    }

    /**
     * Create a new Choice for a Question
     *
     * @param description field of that Choice
     * @param score       score for that Choice
     */
    public Choice(String description, int score) {
        super();
        this.description = description;
        this.score = score;
    }

    public Choice() {
        super();
    }

    public static ArrayList<Choice> getAllChoices() {
        List<Choice> list = new Select().from(Choice.class).orderBy("guid DESC").execute();
        ArrayList<Choice> choices = new ArrayList<>();
        choices.addAll(list);
        return choices;
    }

    /**
     * Get the patientName of the Choice
     *
     * @return patientName of the Choice
     */
    public String getName() {
        return name;
    }

    /**
     * Set the patientName of the Choice
     *
     * @param name name of the choice
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the score for this Choice
     *
     * @return score
     */
    public double getScore() {
        return score;
    }

    /**
     * Set the score for this Choice
     *
     * @param score score
     */
    public void setScore(double score) {
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

    public static Choice getChoiceByID(String choiceID) {
        return new Select()
                .from(Choice.class)
                .where("guid = ?", choiceID)
                .executeSingle();
    }

    @Override
    public String toString() {
        return "Choice{" +
                "guid='" + guid + '\'' +
                ", area='" + name + '\'' +
                ", field='" + description + '\'' +
                ", score=" + score +
                ", yes=" + yes +
                ", no=" + no +
                ", question=" + question +
                '}';
    }
}
