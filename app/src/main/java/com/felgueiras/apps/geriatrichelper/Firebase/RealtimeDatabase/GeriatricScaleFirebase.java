package com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase;


import android.graphics.Bitmap;

import com.felgueiras.apps.geriatrichelper.DataTypes.Scales;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rafael on 15-09-2016.
 */
public class GeriatricScaleFirebase implements Serializable {

    /**
     * ID of the Test
     */
    @Expose
    String guid;
    /**
     * Name of the scaleID
     */
    @Expose
    String scaleName;
    /**
     * Type of scaleID
     */
    @Expose
    String area;
    /**
     * Subcategory if applied
     */
    @Expose
    String subCategory;
    /**
     * Textual field of the procedure.
     */
    @Expose
    String description;
    /**
     * Numerical result of the GeriatricScale
     */
    @Expose
    double result;
    /**
     * Time it takes to be completed.
     */
    @Expose
    int time;
    /**
     * Session to which it belongs.
     */
    @Expose
    String sessionID;

    @Expose
    boolean completed = false;

    @Expose
    String shortName;

    @Expose
    boolean singleQuestion;

    @Expose
    private boolean multipleChoice;


    /**
     * Notes for a scaleID, can explain why the result is this one.
     */
    @Expose
    String notes;

    @Expose
    boolean alreadyOpened;

    @Expose
    boolean containsPhoto;

    Bitmap photo;


    @Expose
    String answer;


    @Expose
    String photoPath;

    @Expose
    String videoPath;

    @Expose
    ArrayList<String> questionsIDs = new ArrayList<>();


    private String key;
    private boolean containsVideo;


//    public static ArrayList<GeriatricScaleFirebase> getAllScales() {
//        List<GeriatricScaleFirebase> list = new Select().from(GeriatricScaleFirebase.class).orderBy("guid DESC").execute();
//        ArrayList<GeriatricScaleFirebase> scales = new ArrayList<>();
//        scales.addAll(list);
//        return scales;
//    }


    /*
      Get the Questions from this Test.

      @return ArrayList of Question for the Test
     */
//    public ArrayList<Question> getQuestionsFromScale() {
//        ArrayList<Question> questions = new ArrayList<>();
//        List<Question> questionsList = getMany(Question.class, "scaleID");
//        questions.addAll(questionsList);
//        return questions;
//    }

    /**
     * Get the Session to which this scaleID belongs to.
     *
     * @return
     */
    public String getSessionID() {
        return sessionID;
    }

    /**
     * Set the Session to which this Test belongs to.
     *
     * @param session
     */
    public void setSessionID(String session) {
        this.sessionID = session;
    }

    /**
     * Create a new GeriatricScale
     *
     * @param testName    date of the scaleID
     * @param category    area of the scaleID
     * @param subCategory subcategory of the scaleID
     * @param description field of the scaleID
     */
    public GeriatricScaleFirebase(String testName, String category, String subCategory, String description) {
        super();
        this.area = category;
        this.scaleName = testName;
        this.subCategory = subCategory;
        this.description = description;
        this.containsPhoto = false;
    }

    public GeriatricScaleFirebase() {
        super();
    }


    public String getScaleName() {
        return scaleName;
    }

    public void setScaleName(String testName) {
        this.scaleName = testName;
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
        ret += area + " - " + scaleName + " - " + result;
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

    /*
      Get the GeriatricScale with a given ID

      @param testID
     * @return
     */
//    public static GeriatricScaleFirebase getTestByID(String testID) {
//        return new Select()
//                .from(GeriatricScaleFirebase.class)
//                .where("guid = ?", testID)
//                .executeSingle();
//    }


    /*
      Get the result for a Test.

      @return
     */
//    public double generateScaleResult() {
//        double res = 0;
//        ArrayList<Question> questionsFromTest = getQuestionsFromScale();
//
//        if (singleQuestion) {
//            //system.out.println("SINGLE");
//            ScoringNonDB scoring = Scales.getScaleByName(this.getScaleName()).getScoring();
//            ArrayList<GradingNonDB> valuesBoth = scoring.getValuesBoth();
//            for (GradingNonDB grade : valuesBoth) {
//                if (grade.getGrade().equals(answer)) {
//                    this.result = Double.parseDouble(grade.getScore());
//                    this.save();
//                    return Double.parseDouble(grade.getScore());
//                }
//            }
//        } else {
//            for (Question question : questionsFromTest) {
//                // in the Hamilton scale, only the first 17 questions make up the result
//                if (scaleName.equals(Constants.test_name_hamilton) &&
//                        questionsFromTest.indexOf(question) > 16)
//                    break;
//                /**
//                 * Yes/no Question
//                 */
//                if (question.isYesOrNo()) {
//                    String selectedYesNoChoice = question.getSelectedYesNoChoice();
//                    if (selectedYesNoChoice.equals("yes")) {
//                        res += question.getYesValue();
//                    } else {
//                        res += question.getNoValue();
//                    }
//                }
//                /**
//                 * Right/ wrong question
//                 */
//                else if (question.isRightWrong()) {
//                    if (question.getSelectedRightWrong().equals("right"))
//                        res += 1;
//                }
//                /**
//                 * Numerical question.
//                 */
//                else if (question.isNumerical()) {
//                    System.out.println("Numerical");
//                    res += question.getAnswerNumber();
//                }
//                /**
//                 * Multiple Choice Question
//                 */
//                else {
//                    // get the selected Choice
//                    String selectedChoice = question.getSelectedChoice();
//                    //system.out.println("Selected choice " + selectedChoice);
//                    ArrayList<Choice> choices = question.getChoicesForQuestion();
//                    //system.out.println("size " + choices.size());
//                    for (Choice c : choices) {
//                        if (c.getName().equals(selectedChoice)) {
//                            //system.out.println(c.toString());
//                            res += c.getScore();
//                        }
//                    }
//
//                }
//            }
//        }
//
//        if (scaleName.equals(Constants.test_name_mini_nutritional_assessment_global)) {
//            // check if triagem is already answered
//            Log.d("Nutritional", "Global pressed");
//
//            ArrayList<GeriatricScaleFirebase> allScales = GeriatricScaleFirebase.getAllScales();
//
//            GeriatricScaleFirebase triagem = session.getScaleByName(Constants.test_name_mini_nutritional_assessment_triagem);
//            res += triagem.generateScaleResult();
//        }
//        if (scaleName.equals(Constants.test_name_set_set)) {
//            // result is the value from the last question (scoring)
//            res = questionsFromTest.get(questionsFromTest.size() - 1).getAnswerNumber();
//        }
//        this.result = res;
//        this.save();
//
//        return res;
//    }

    /**
     * Set the addNotesButton for this scaleID,
     *
     * @param notes
     */
    public void setNotes(String notes) {
        //system.out.println("Adding note for this scaleID");
        this.notes = notes;
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


    /**
     * Get the scale instances from a PATIENT's sessions.
     *
//     * @param patientSessions
//     * @param scaleName
     * @return
     */
//    public static ArrayList<GeriatricScaleFirebase> getScaleInstancesForPatient(ArrayList<Session> patientSessions, String scaleName) {
//        ArrayList<GeriatricScaleFirebase> testInstances = new ArrayList<>();
//        // get instances for that scaleID
//        for (Session currentSession : patientSessions) {
//            List<GeriatricScaleFirebase> testsFromSession = currentSession.getScalesFromSession();
//            for (GeriatricScaleFirebase currentTest : testsFromSession) {
//                if (currentTest.getScaleName().equals(scaleName)) {
//                    testInstances.add(currentTest);
//                }
//            }
//        }
//        return testInstances;
//    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<String> getQuestionsIDs() {
        return questionsIDs;
    }

    public void setQuestionsIDs(ArrayList<String> questionsIDs) {
        this.questionsIDs = questionsIDs;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean photos() {
        return containsPhoto;
    }

    public void setContainsPhoto(boolean containsPhoto) {
        this.containsPhoto = containsPhoto;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public void setContainsVideo(boolean containsVideo) {
        this.containsVideo = containsVideo;
    }

    public boolean isContainsVideo() {
        return containsVideo;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public boolean isMultipleChoice() {
        return multipleChoice;
    }

    public void setMultipleChoice(boolean multipleChoice) {
        this.multipleChoice = multipleChoice;
    }
}
