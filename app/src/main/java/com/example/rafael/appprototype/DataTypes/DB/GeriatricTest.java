package com.example.rafael.appprototype.DataTypes.DB;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.example.rafael.appprototype.DataTypes.NonDB.GradingNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.ScoringNonDB;
import com.example.rafael.appprototype.DataTypes.Scales;

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
     * Textual field of the procedure.
     */
    @Column(name = "field")
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
    double result;
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

    @Column(name = "shortName")
    String shortName;

    @Column(name = "singleQuestion")
    boolean singleQuestion;

    /**
     * Notes for a test, can explain why the result is this one.
     */
    @Column(name = "notes")
    String notes;

    @Column(name = "alreadyOpened")
    boolean alreadyOpened;

    @Column(name = "answer")
    String answer;


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
     * @param description field of the test
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

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
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
        return Scales.getShortName(this.getTestName());
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
     *
     * @return
     */
    public double generateTestResult() {
        System.out.println("Generating test result!!!");
        //system.out.println("Getting test result...");
        double res = 0;
        ArrayList<Question> questionsFromTest = getQuestionsFromTest();

        if (singleQuestion) {
            //system.out.println("SINGLE");
            ScoringNonDB scoring = Scales.getTestByName(this.getTestName()).getScoring();
            ArrayList<GradingNonDB> valuesBoth = scoring.getValuesBoth();
            for (GradingNonDB grade : valuesBoth) {
                if (grade.getGrade().equals(answer)) {
                    this.result = Double.parseDouble(grade.getScore());
                    this.save();
                    return Double.parseDouble(grade.getScore());
                }
            }
        } else {
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
                    String selectedChoice = question.getSelectedChoice();
                    //system.out.println("Selected choice " + selectedChoice);
                    ArrayList<Choice> choices = question.getChoicesForQuestion();
                    //system.out.println("size " + choices.size());
                    for (Choice c : choices) {
                        if (c.getName().equals(selectedChoice)) {
                            //system.out.println(c.toString());
                            res += c.getScore();
                        }
                    }

                }
            }
        }
        this.result = res;
        this.save();

        return res;
    }

    /**
     * Set the notes for this test,
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
