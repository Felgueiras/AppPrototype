package com.felgueiras.apps.geriatric_helper.DataTypes.NonDB;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rafael on 30-09-2016.
 */
public class QuestionCategory implements Serializable {


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
    private ArrayList<QuestionNonDB> questions;

    /**
     * Notes about this category, when present.
     */
    String notes;


    /**
     * Constructor for the QuestionCategory data type.
     *
     * @param category
     */
    public QuestionCategory(String category) {
        this.category = category;
        this.questions = new ArrayList<>();
        this.description = "";
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

    public String getName() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void addQuestion(QuestionNonDB question) {
        question.setNumber(questions.size());
        this.questions.add(question);
    }

    public ArrayList<QuestionNonDB> getQuestions() {
        return questions;
    }

    /**
     * Get the question index for a question inside a area.
     *
     * @param category
     * @param questionInCategory
     * @param testNonDB
     * @return
     */
    public static int getQuestionIndex(int category, int questionInCategory, GeriatricScaleNonDB testNonDB) {
        int index = 0;
        for (int i = 0; i < category; i++) {
            index += testNonDB.getQuestionsCategories().get(i).getQuestions().size();
        }
        index += questionInCategory;
        return index;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
