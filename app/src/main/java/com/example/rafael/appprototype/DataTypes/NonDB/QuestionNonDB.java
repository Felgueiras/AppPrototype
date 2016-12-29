package com.example.rafael.appprototype.DataTypes.NonDB;


import java.util.ArrayList;

/**
 * Created by rafael on 30-09-2016.
 */
public class QuestionNonDB {


    String category;
    /**
     * Indicates if a question has as answer "right" or "wrong".
     */
    boolean rightWrong;
    /**
     * Textual description of the question or single tag
     */
    String description;
    /**
     * Different answer choices for the question.
     */
    ArrayList<ChoiceNonDB> choices = new ArrayList<>();
    /**
     * Boolean that signals if it is a yes or no question.
     */
    boolean yesOrNo;


    /**
     * Create a new Question
     *
     * @param description description of the Question
     * @param yesOrNo     indicates if it is a yes or no Question or not
     */
    public QuestionNonDB(String description, boolean yesOrNo) {
        this.description = description;
        this.yesOrNo = yesOrNo;
    }

    public QuestionNonDB(String description, String category) {
        this.description = description;
        this.yesOrNo = false;
        this.rightWrong = true;
        this.category = category;
    }

    public void setRightWrong(boolean rightWrong) {
        this.rightWrong = rightWrong;
    }

    public boolean isRightWrong() {
        return rightWrong;
    }

    public QuestionNonDB(String description, int yes, int no) {
        this.description = description;
        this.choices.add(new ChoiceNonDB(yes, no));
        this.yesOrNo = true;
    }

    public QuestionNonDB() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<ChoiceNonDB> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<ChoiceNonDB> choices) {
        this.choices = choices;
    }


    public boolean isYesOrNo() {
        return yesOrNo;
    }

    public void setYesOrNo(boolean yesOrNo) {
        this.yesOrNo = yesOrNo;
    }

    public int getYesScore() {
        ChoiceNonDB choiceNonDB = this.choices.get(0);
        return choiceNonDB.getYes();
    }

    public int getNoScore() {
        ChoiceNonDB choiceNonDB = this.choices.get(0);
        return choiceNonDB.getNo();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
