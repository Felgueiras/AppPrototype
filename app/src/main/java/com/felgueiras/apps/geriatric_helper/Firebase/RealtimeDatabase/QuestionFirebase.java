package com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase;


import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rafael on 30-09-2016.
 */
public class QuestionFirebase implements Serializable {

    @Expose
    String guid;
    /**
     * Textual field of the question or single tag
     */
    @Expose
    String description;
    /**
     * Boolean that signals if it is a yes or no question.
     */
    @Expose
    boolean yesOrNo;

    @Expose
    boolean rightWrong;

    @Expose
    String scaleID;
    /**
     * If yes or no question, holds the Choice
     */
    @Expose
    String selectedYesNoChoice;

    @Expose
    int yesValue;

    @Expose
    int noValue;

    @Expose
    boolean answered;

    @Expose
    String selectedChoice;

    @Expose
    String selectedRightWrong;

    @Expose
    boolean numerical;

    @Expose
    int answerNumber;

    @Expose
    private boolean multipleTextInput;

    @Expose
    private String textAnswer;

    @Expose
    private ArrayList<String> choicesIDs = new ArrayList<>();

    String key;

    /**
     * Create a new Question
     *
     * @param description field of the Question
     * @param yesOrNo     indicates if it is a yes or no Question or not
     */
    public QuestionFirebase(String description, boolean yesOrNo) {
        super();
        this.description = description;
        this.yesOrNo = yesOrNo;
    }

    public QuestionFirebase() {
        super();
    }

//    public static ArrayList<QuestionFirebase> getAllQuestions() {
//        List<QuestionFirebase> list = new Select().from(QuestionFirebase.class).orderBy("guid DESC").execute();
//        ArrayList<QuestionFirebase> questions = new ArrayList<>();
//        questions.addAll(list);
//        return questions;
//    }


    /**
     * Get all possible Choices for this Question
     *
     * @return Choices
     */
//    public ArrayList<Choice> getChoicesForQuestion() {
//        ArrayList<Choice> questions = new ArrayList<>();
//        List<Choice> questionsList = getMany(Choice.class, "question");
//        questions.addAll(questionsList);
//        return questions;
//    }
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

    public String getScaleID() {
        return scaleID;
    }

    public void setScaleID(String scaleID) {
        this.scaleID = scaleID;
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
     * <p>
     * //     * @param questionID ID of the Question
     *
     * @return Question with that ID value
     */
//    public static QuestionFirebase getQuestionByID(String questionID) {
//        return new Select()
//                .from(QuestionFirebase.class)
//                .where("guid = ?", questionID)
//                .executeSingle();
//    }
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


    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }


    public void addChoiceID(String choiceID) {
        choicesIDs.add(choiceID);
        FirebaseDatabaseHelper.updateQuestion(this);
    }
}

