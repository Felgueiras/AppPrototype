package com.felgueiras.apps.geriatric_helper.DataTypes.DB;


import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GradingNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.ScoringNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.Scales;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafael on 15-09-2016.
 */
@Table(name = "GeriatricScale")
public class GeriatricScale extends Model implements Serializable {

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
     * Numerical result of the GeriatricScale
     */
    @Expose
    @Column(name = "result")
    double result;
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


    public static ArrayList<GeriatricScale> getAllScales() {
        List<GeriatricScale> list = new Select().from(GeriatricScale.class).orderBy("guid DESC").execute();
        ArrayList<GeriatricScale> scales = new ArrayList<>();
        scales.addAll(list);
        return scales;
    }


    /**
     * Get the Questions from this Test.
     *
     * @return ArrayList of Question for the Test
     */
    public ArrayList<Question> getQuestionsFromScale() {
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
    public GeriatricScale(String testName, String category, String subCategory, String description) {
        super();
        this.area = category;
        this.testName = testName;
        this.subCategory = subCategory;
        this.description = description;
    }

    public GeriatricScale() {
        super();
    }


    public String getScaleName() {
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
        ret += area + " - " + testName + " - " + result;
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
    public static GeriatricScale getTestByID(String testID) {
        return new Select()
                .from(GeriatricScale.class)
                .where("guid = ?", testID)
                .executeSingle();
    }


    /**
     * Get the result for a Test.
     *
     * @return
     */
    public double generateTestResult() {
        double res = 0;
        ArrayList<Question> questionsFromTest = getQuestionsFromScale();

        if (singleQuestion) {
            //system.out.println("SINGLE");
            ScoringNonDB scoring = Scales.getScaleByName(this.getScaleName()).getScoring();
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
                // in the Hamilton scale, only the first 17 questions make up the result
                if (testName.equals(Constants.test_name_hamilton) &&
                        questionsFromTest.indexOf(question) > 16)
                    break;
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
                 * Right/ wrong question
                 */
                else if (question.isRightWrong()) {
                    if (question.getSelectedRightWrong().equals("right"))
                        res += 1;
                }
                /**
                 * Numerical question.
                 */
                else if (question.isNumerical()) {
                    System.out.println("Numerical");
                    res += question.getAnswerNumber();
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

        if (testName.equals(Constants.test_name_mini_nutritional_assessment_global)) {
            // check if triagem is already answered
            Log.d("Nutritional", "Global pressed");

            ArrayList<GeriatricScale> allScales = GeriatricScale.getAllScales();

            GeriatricScale triagem = session.getScaleByName(Constants.test_name_mini_nutritional_assessment_triagem);
            res += triagem.generateTestResult();
        }
        if (testName.equals(Constants.test_name_set_set)) {
            // result is the value from the last question (scoring)
            res = questionsFromTest.get(questionsFromTest.size() - 1).getAnswerNumber();
        }
        this.result = res;
        this.save();

        return res;
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

    /**
     * Get the scale instances from a PATIENT's sessions.
     *
     * @param patientSessions
     * @param scaleName
     * @return
     */
    public static ArrayList<GeriatricScale> getScaleInstancesForPatient(ArrayList<Session> patientSessions, String scaleName) {
        ArrayList<GeriatricScale> testInstances = new ArrayList<>();
        // get instances for that test
        for (Session currentSession : patientSessions) {
            List<GeriatricScale> testsFromSession = currentSession.getScalesFromSession();
            for (GeriatricScale currentTest : testsFromSession) {
                if (currentTest.getScaleName().equals(scaleName)) {
                    testInstances.add(currentTest);
                }
            }
        }
        return testInstances;
    }
}
