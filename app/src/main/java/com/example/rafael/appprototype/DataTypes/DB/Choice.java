package com.example.rafael.appprototype.DataTypes.DB;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

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
    @Column(name = "patientName")
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
    double score;
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
    @Column(name = "question", onDelete = Column.ForeignKeyAction.CASCADE)
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

    /**
     * Create a new Choice for a Question
     *
     * @param description description of that Choice
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
     * @param name
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
     * @return
     */
    public double getScore() {
        return score;
    }

    /**
     * Set the score for this Choice
     *
     * @param score
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
}
