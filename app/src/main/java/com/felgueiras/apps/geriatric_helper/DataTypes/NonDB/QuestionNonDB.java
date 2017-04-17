package com.felgueiras.apps.geriatric_helper.DataTypes.NonDB;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rafael on 30-09-2016.
 */
public class QuestionNonDB implements Serializable {

    /**
     * Is this question only for women?
     */
    boolean onlyForWomen;

    String category;
    /**
     * Indicates if a question has as answer "right" or "wrong".
     */
    boolean rightWrong;
    /**
     * Textual field of the question or single tag
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
    private int number;
    private boolean numerical;
    private boolean multipleTextInput;
    private int numericalMax;
    private int image;


    /**
     * Create a new Question
     *
     * @param description field of the Question
     * @param yesOrNo     indicates if it is a yes or no Question or not
     */
    public QuestionNonDB(String description, boolean yesOrNo) {
        this.description = description;
        this.yesOrNo = yesOrNo;
    }

    public QuestionNonDB(String description) {
        this.description = description;
        this.yesOrNo = false;
        this.rightWrong = true;
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

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public boolean isOnlyForWomen() {
        return onlyForWomen;
    }

    public void setOnlyForWomen(boolean onlyForWomen) {
        this.onlyForWomen = onlyForWomen;
    }

    public void setNumerical(boolean numerical) {
        this.numerical = numerical;
    }

    public boolean isNumerical() {
        return numerical;
    }

    public void setMultipleTextInput(boolean multipleTextInput) {
        this.multipleTextInput = multipleTextInput;
    }

    public boolean isMultipleTextInput() {
        return multipleTextInput;
    }

    public void setNumericalMax(int numericalMax) {
        this.numericalMax = numericalMax;
    }

    public int getNumericalMax() {
        return numericalMax;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }


}
