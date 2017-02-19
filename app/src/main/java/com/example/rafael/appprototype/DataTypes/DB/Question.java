package com.example.rafael.appprototype.DataTypes.DB;


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
@Table(name = "Questions")
public class Question extends Model {

    @Expose
    @Column(name = "guid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    String guid;
    /**
     * Textual field of the question or single tag
     */
    @Expose
    @Column(name = "field")
    String description;
    /**
     * Boolean that signals if it is a yes or no question.
     */
    @Expose
    @Column(name = "yesOrNo")
    boolean yesOrNo;

    @Expose
    @Column(name = "rightWrong")
    boolean rightWrong;

    @Expose
    @Column(name = "test", onDelete = Column.ForeignKeyAction.CASCADE)
    GeriatricScale test;
    /**
     * If yes or no question, holds the Choice
     */
    @Expose
    @Column(name = "selectedYesNoChoice")
    String selectedYesNoChoice;

    @Expose
    @Column(name = "yesValue")
    int yesValue;

    @Expose
    @Column(name = "noValue")
    int noValue;

    @Expose
    @Column(name = "answered")
    boolean answered;

    @Expose
    @Column(name = "selectedChoice")
    String selectedChoice;

    @Expose
    @Column(name = "selectedRightWrong")
    String selectedRightWrong;

    @Expose
    @Column(name = "numerical")
    boolean numerical;

    @Expose
    @Column(name = "answerNumber")
    int answerNumber;

    @Expose
    @Column(name = "multipleTextInput")
    private boolean multipleTextInput;

    @Expose
    @Column(name = "textAnswer")
    private String textAnswer;

    /**
     * Create a new Question
     *
     * @param description field of the Question
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
     *
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

    public GeriatricScale getTest() {
        return test;
    }

    public void setTest(GeriatricScale test) {
        this.test = test;
    }

    public String getSelectedChoice() {
        return selectedChoice;
    }

    public void setSelectedChoice(String selectedChoice) {
        //system.out.println(selectedChoice);
        this.selectedChoice = selectedChoice;
    }

    public boolean isRightWrong() {
        return rightWrong;
    }

    public void setRightWrong(boolean rightWrong) {
        this.rightWrong = rightWrong;
    }

    /**
     * Get guid for the Question
     *
     * @return
     */
    public String getGuid() {
        return guid;
    }

    /**
     * Set guid for the Question
     *
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

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    /**
     * Get a Question by its ID
     *
     * @param questionID ID of the Question
     * @return Question with that ID value
     */
    public static Question getQuestionByID(String questionID) {
        return new Select()
                .from(Question.class)
                .where("guid = ?", questionID)
                .executeSingle();
    }

    @Override
    public String toString() {
        String str = "";
        str += "ID - " + guid;
        str += "Description - " + description;
        str += "Already answered? " + answered;
        str += "Right/wrong " + selectedRightWrong;
        str += "Yes/no " + yesOrNo;
        str += "Numerical " + numerical;
        str += "Number " + answerNumber;
        return str;
    }

    public void setSelectedRightWrong(String selectedRightWrong) {
        this.selectedRightWrong = selectedRightWrong;
        //system.out.println(selectedRightWrong);
    }

    public String getSelectedRightWrong() {
        return this.selectedRightWrong;
    }

    public void setNumerical(boolean numerical) {
        this.numerical = numerical;
    }

    public boolean isNumerical() {
        return numerical;
    }

    public int getAnswerNumber() {
        return answerNumber;
    }

    public void setAnswerNumber(int answerNumber) {
        this.answerNumber = answerNumber;
    }

    public void setMultipleTextInput(boolean multipleTextInput) {
        this.multipleTextInput = multipleTextInput;
    }

    public boolean isMultipleTextInput() {
        return multipleTextInput;
    }

    public void setTextAnswer(String textAnswer) {
        this.textAnswer = textAnswer;
    }

    public String getTextAnswer() {
        return textAnswer;
    }
}

