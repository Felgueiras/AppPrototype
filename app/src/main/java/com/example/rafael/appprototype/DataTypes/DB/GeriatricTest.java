package com.example.rafael.appprototype.DataTypes.DB;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.example.rafael.appprototype.DataTypes.StaticTestDefinition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafael on 15-09-2016.
 */
@Table(name = "GeriatricTests")
public class GeriatricTest extends Model implements Serializable {

    /**
     * ID of the Test
     */
    @Column(name = "guid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    String guid;
    /**
     * Name of the test
     */
    @Column(name = "testName")
    String testName;
    /**
     * Type of test
     */
    @Column(name = "category")
    String category;
    /**
     * Subcategory if applied
     */
    @Column(name = "subCategory")
    String subCategory;
    /**
     * Textual description of the procedure.
     */
    @Column(name = "description")
    String description;
    /**
     * Scoring definition, min and man score and categories of scores.
     */
    @Column(name = "scoring", onDelete = Column.ForeignKeyAction.CASCADE)
    Scoring scoring;
    /**
     * Numerical result of the GeriatricTest
     */
    @Column(name = "result")
    int result;
    /**
     * Time it takes to be completed.
     */
    @Column(name = "time")
    int time;
    /**
     * Session to which it belongs.
     */
    @Column(name = "session", onDelete = Column.ForeignKeyAction.CASCADE)
    Session session;

    @Column(name = "completed")
    boolean completed;

    @Column(name="shortName")
    String shortName;

    @Column(name="alreadyOpened")
    boolean alreadyOpened;



    /**
     * Get the Questions from this Test.
     *
     * @return ArrayList of Question for the Test
     */
    public ArrayList<Question> getQuestionsFromTest() {
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
     * Create a new GeriatricTest
     *
     * @param testName    date of the test
     * @param category    category of the test
     * @param subCategory subcategory of the test
     * @param description description of the test
     */
    public GeriatricTest(String testName, String category, String subCategory, String description) {
        super();
        this.category = category;
        this.testName = testName;
        this.subCategory = subCategory;
        this.description = description;
    }

    public GeriatricTest() {
        super();
    }


    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getType() {
        return category;
    }

    public void setType(String type) {
        this.category = type;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public Scoring getScoring() {
        return scoring;
    }

    public void setScoring(Scoring scoring) {
        this.scoring = scoring;
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
        return StaticTestDefinition.getShortName(this.getTestName());
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public String toString() {
        String ret = "";
        ret += category + " - " + testName + " - " + result;
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
     * Get the GeriatricTest with a given ID
     *
     * @param testID
     * @return
     */
    public static GeriatricTest getTestByID(String testID) {
        return new Select()
                .from(GeriatricTest.class)
                .where("guid = ?", testID)
                .executeSingle();
    }

    /**
     * Get the result for a Test.
     * @return
     */
    public int getTestResult() {
        int res = 0;
        ArrayList<Question> questionsFromTest = getQuestionsFromTest();

        for (Question question : questionsFromTest) {
            /**
             * Yes/no Question
             */
            if (question.isYesOrNo()) {
                String selectedYesNoChoice = question.getSelectedYesNoChoice();
                if (selectedYesNoChoice.equals("yes")) {
                    res += question.getYesValue();

                } else {
                    res += question.getNoValue();
                }
            }
            /**
             * Multiple Choice Question
             */
            else {
                // get the selected Choice
                Choice selectedChoice = question.getChoicesForQuestion().get(question.getSelectedChoice());
                res += selectedChoice.getScore();
            }
        }
        return res;
    }
}
