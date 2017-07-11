package com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase;


import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by rafael on 30-09-2016.
 */
public class ChoiceFirebase implements Serializable {
    @Expose
    String guid;
    /**
     * Name of the choice.
     */
    @Expose
    String name;
    /**
     * Description of the choice.
     */
    @Expose
    String description;
    /**
     * Score for that choice for that question;
     */
    @Expose
    double score;
    /**
     * Score if yes answer.
     */
    @Expose
    int yes;
    /**
     * Score if no question.
     */
    @Expose
    int no;

    /**
     * Question for this Choice
     */
    @Expose
    String questionID;


    private String key;

    /**
     * Create a new Choice for a Question
     *
     * @param name        date of the choice
     * @param description field
     * @param score       score for that Choice
     */
    public ChoiceFirebase(String name, String description, int score) {
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
    public ChoiceFirebase(String description, int score) {
        super();
        this.description = description;
        this.score = score;
    }

    public ChoiceFirebase() {
        super();
    }

//    public static ArrayList<ChoiceFirebase> getAllChoices() {
//        List<ChoiceFirebase> list = new Select().from(ChoiceFirebase.class).orderBy("guid DESC").execute();
//        ArrayList<ChoiceFirebase> choices = new ArrayList<>();
//        choices.addAll(list);
//        return choices;
//    }

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

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

//    public static ChoiceFirebase getChoiceByID(String choiceID) {
//        return new Select()
//                .from(ChoiceFirebase.class)
//                .where("guid = ?", choiceID)
//                .executeSingle();
//    }

    @Override
    public String toString() {
        return "Choice{" +
                "guid='" + guid + '\'' +
                ", area='" + name + '\'' +
                ", field='" + description + '\'' +
                ", score=" + score +
                ", yes=" + yes +
                ", no=" + no +
                ", question=" + questionID +
                '}';
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
