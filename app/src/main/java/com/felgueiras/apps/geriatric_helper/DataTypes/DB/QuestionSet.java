package com.felgueiras.apps.geriatric_helper.DataTypes.DB;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.felgueiras.apps.geriatric_helper.DataTypes.Scales;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafael on 15-09-2016.
 */
@Table(name = "QuestionSet")
public class QuestionSet extends Model implements Serializable {

    /**
     * ID of the Test
     */
    @Expose
    @Column(name = "guid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    String guid;
    /**
     * Name of the test
     */
    @Expose
    @Column(name = "testName")
    String testName;
    /**
     * Type of test
     */
    @Expose
    @Column(name = "area")
    String area;
    /**
     * Subcategory if applied
     */
    @Expose
    @Column(name = "subCategory")
    String subCategory;
    /**
     * Textual field of the procedure.
     */
    @Expose
    @Column(name = "field")
    String description;

    /**
     * Time it takes to be completed.
     */
    @Expose
    @Column(name = "time")
    int time;
    /**
     * Session to which it belongs.
     */
    @Expose
    @Column(name = "session", onDelete = Column.ForeignKeyAction.CASCADE)
    Session session;

    @Expose
    @Column(name = "completed")
    boolean completed;

    @Expose
    @Column(name = "shortName")
    String shortName;

    @Expose
    @Column(name = "singleQuestion")
    boolean singleQuestion;

    /**
     * Notes for a test, can explain why the result is this one.
     */
    @Expose
    @Column(name = "addNotesButton")
    String notes;

    @Expose
    @Column(name = "alreadyOpened")
    boolean alreadyOpened;

    @Expose
    @Column(name = "answer")
    String answer;




    /**
     * Get the Questions from this Test.
     *
     * @return ArrayList of Question for the Test
     */
    public ArrayList<Question> getQuestionsFromQuestionSet() {
        ArrayList<Question> questions = new ArrayList<>();
        List<Question> questionsList = getMany(Question.class, "test");
        questions.addAll(questionsList);
        return questions;
    }

    /**
     * Get the Session to which this test belongs to.
     *
     * @return
     */
    public Session getSession() {
        return session;
    }

    /**
     * Set the Session to which this Test belongs to.
     *
     * @param session
     */
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * Create a new GeriatricScale
     *
     * @param testName    date of the test
     * @param category    area of the test
     * @param subCategory subcategory of the test
     * @param description field of the test
     */
    public QuestionSet(String testName, String category, String subCategory, String description) {
        super();
        this.area = category;
        this.testName = testName;
        this.subCategory = subCategory;
        this.description = description;
    }

    public QuestionSet() {
        super();
    }


    public String getScaleName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getType() {
        return area;
    }

    public void setType(String type) {
        this.area = type;
    }


    public String getArea() {
        return area;
    }

    public void setArea(String category) {
        this.area = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }


    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }


    public String getShortName() {
        return Scales.getShortName(this.getScaleName());
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public String toString() {
        String ret = "";
        ret += area + " - " + testName;
        return ret;
    }

    public boolean isAlreadyOpened() {
        return alreadyOpened;
    }

    public void setAlreadyOpened(boolean alreadyOpened) {
        this.alreadyOpened = alreadyOpened;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getGuid() {
        return guid;
    }

    /**
     * Get the GeriatricScale with a given ID
     *
     * @param testID
     * @return
     */
    public static QuestionSet getTestByID(String testID) {
        return new Select()
                .from(QuestionSet.class)
                .where("guid = ?", testID)
                .executeSingle();
    }




    /**
     * Set the addNotesButton for this test,
     *
     * @param notes
     */
    public void setNotes(String notes) {
        //system.out.println("Adding note for this test");
        this.notes = notes;
    }

    public boolean hasNotes() {
        return notes != null;
    }

    public String getNotes() {
        return notes;
    }


    public boolean getSingleQuestion() {
        return singleQuestion;
    }

    public void setSingleQuestion(boolean singleQuestion) {
        this.singleQuestion = singleQuestion;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
