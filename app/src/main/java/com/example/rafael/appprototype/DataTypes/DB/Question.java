package com.example.rafael.appprototype.DataTypes.DB;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafael on 30-09-2016.
 */
@Table(name = "Questions")
public class Question extends Model {

    @Column(name = "guid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    String guid;
    /**
     * Textual description of the question or single tag
     */
    @Column(name = "description")
    String description;
    /**
     * Boolean that signals if it is a yes or no question.
     */
    @Column(name = "yesOrNo")
    boolean yesOrNo;

    @Column(name = "test")
    GeriatricTest test;
    /**
     * If yes or no question, holds the Choice
     */
    @Column(name = "selectedYesNoChoice")
    String selectedYesNoChoice;
    @Column(name = "yesValue")
    int yesValue;
    @Column(name = "noValue")
    int noValue;

    /**
     * Create a new Question
     *
     * @param description description of the Question
     * @param yesOrNo     indicates if it is a yes or no Question or not
     */
    public Question(String description, boolean yesOrNo) {
        super();
        this.description = description;
        this.yesOrNo = yesOrNo;
    }

    public Question() {
        super();
    }


    /**
     * Get all possible Choices for this Question
     * @return Choices
     */
    public ArrayList<Choice> getChoicesForQuestion() {
        ArrayList<Choice> questions = new ArrayList<>();
        List<Choice> questionsList = getMany(Choice.class, "question");
        questions.addAll(questionsList);
        return questions;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public boolean isYesOrNo() {
        return yesOrNo;
    }

    public void setYesOrNo(boolean yesOrNo) {
        this.yesOrNo = yesOrNo;
    }

    public GeriatricTest getTest() {
        return test;
    }

    public void setTest(GeriatricTest test) {
        this.test = test;
    }

    /**
     * Get guid for the Question
     * @return
     */
    public String getGuid() {
        return guid;
    }

    /**
     * Set guid for the Question
     * @param guid
     */
    public void setGuid(String guid) {
        this.guid = guid;
    }


    public String getSelectedYesNoChoice() {
        return selectedYesNoChoice;
    }

    public void setSelectedYesNoChoice(String selectedYesNoChoice) {
        this.selectedYesNoChoice = selectedYesNoChoice;
    }

    public int getYesValue() {
        return yesValue;
    }

    public void setYesValue(int yesValue) {
        this.yesValue = yesValue;
    }

    public int getNoValue() {
        return noValue;
    }

    public void setNoValue(int noValue) {
        this.noValue = noValue;
    }

    /**
     * Get a Question by its ID
     * @param questionID ID of the Question
     * @return Question with that ID value
     */
    public static Question getQuestionByID(String questionID) {
        return new Select()
                .from(Question.class)
                .where("guid = ?", questionID)
                .executeSingle();
    }

}
