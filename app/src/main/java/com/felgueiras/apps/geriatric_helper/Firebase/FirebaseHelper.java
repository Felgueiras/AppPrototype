package com.felgueiras.apps.geriatric_helper.Firebase;

import android.content.Context;
import android.util.Log;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.Criteria.StartCriteria;
import com.felgueiras.apps.geriatric_helper.DataTypes.Criteria.StoppCriteria;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.ChoiceNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GradingNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.ScoringNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.Scales;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.ChoiceFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PrescriptionFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.QuestionFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.SharedPreferencesHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;


/**
 * Created by felgueiras on 08/04/2017.
 */

public class FirebaseHelper {

    private static final String PUBLIC = "/public";
    private static com.google.firebase.database.FirebaseDatabase mFirebaseInstance = null;

    /**
     * Firebase realtime database URL.
     */
    public static final String firebaseURL = "gs://appprototype-bdd27.appspot.com";

    /**
     * Patients table name.
     */
    private static String PATIENTS;

    /**
     * Sessions table name.
     */
    private static String SESSIONS;
    /**
     * Scales table name.
     */
    private static String SCALES;

    /**
     * Questions table name.
     */
    private static String QUESTIONS;

    /**
     * Choices table name.
     */
    private static String CHOICES;
    /**
     * Prescriptions table name.
     */
    private static String PRESCRIPTIONS;


    /**
     * Firebase  public part of the database.
     */
    private static DatabaseReference firebaseTablePublic;

    /**
     * Firebase - patients table.
     */
    public static DatabaseReference firebaseTablePatients;
    /**
     * Firebase - sessions table.
     */
    public static DatabaseReference firebaseTableSessions;
    /**
     * Firebase - scales table.
     */
    public static DatabaseReference firebaseTableScales;
    /**
     * Firebase - questions table.
     */
    public static DatabaseReference firebaseTableQuestions;
    /**
     * Firebase - choices table.
     */
    public static DatabaseReference firebaseTableChoices;
    /**
     * Firebase - prescriptions for patients table.
     */
    public static DatabaseReference firebaseTablePrescriptions;


    /**
     * Patients.
     */
    public static ArrayList<PatientFirebase> patients = new ArrayList<>();
    /**
     * Favorite Patients.
     */
    public static ArrayList<PatientFirebase> favoritePatients = new ArrayList<>();
    /**
     * Sessions.
     */
    public static ArrayList<SessionFirebase> sessions = new ArrayList<>();
    /**
     * Scales.
     */
    public static ArrayList<GeriatricScaleFirebase> scales = new ArrayList<>();
    /**
     * Questions.
     */
    public static ArrayList<QuestionFirebase> questions = new ArrayList<>();

    /**
     * Prescriptions.
     */
    public static ArrayList<PrescriptionFirebase> prescriptions = new ArrayList<>();
    public static boolean canLeaveLaunchScreen = false;
    public static FirebaseRemoteConfig mFirebaseRemoteConfig;
    public static ArrayList<StoppCriteria> stoppCriteria = new ArrayList<>();
    public static ArrayList<StartCriteria> startCriteria = new ArrayList<>();


    /**
     * Create database instance and fetch scales and criteria (upon start)
     * or when there is a new version.
     *
     * @param context
     */
    public static void initializeAndCheckVersions(final Context context) {
        if (Constants.firebaseInstance == null) {
            // allow offline persistence
            com.google.firebase.database.FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            mFirebaseInstance = com.google.firebase.database.FirebaseDatabase.getInstance();
            Constants.firebaseInstance = mFirebaseInstance;
        } else {
            mFirebaseInstance = Constants.firebaseInstance;
        }


        Log.d("Firebase", "Checking scales version");
        firebaseTablePublic = mFirebaseInstance.getReference(FirebaseHelper.PUBLIC);

        FirebaseHelper.firebaseTablePublic.child("scalesVersion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int firScalesVersion = dataSnapshot.getValue(Integer.class);
                Log.d("ScalesVersion", firScalesVersion + "");
                // get version from shared preferences
                int sharedPrefScalesVersion = SharedPreferencesHelper.getScalesVersion(context);
                if (sharedPrefScalesVersion != firScalesVersion) {
                    Log.d("ScalesVersion", "Updating scalesVersion to " + firScalesVersion);
                    SharedPreferencesHelper.setScalesVersion(context, firScalesVersion);
                    // download new scales version
                    canLeaveLaunchScreen = false;
                    FirebaseStorageHelper.downloadScales(context, firebaseTablePublic);
                } else {
                    // same version - no need to donwload
                    Scales.scales.clear();
                    // get scales from SharedPreferences
                    Scales.scales.addAll(SharedPreferencesHelper.getScalesArrayList(context));
                    canLeaveLaunchScreen = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ScalesVersion", "Error");
            }
        });

        FirebaseHelper.firebaseTablePublic.child("criteriaVersion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int firCriteriaVersion = dataSnapshot.getValue(Integer.class);
                Log.d("CriteriaVersion", firCriteriaVersion + "");
                // get version from shared preferences
                if (SharedPreferencesHelper.getCriteriaVersion(context) != firCriteriaVersion) {
                    Log.d("CriteriaVersion", "Updating criteria version to " + firCriteriaVersion);
                    SharedPreferencesHelper.setCriteriaVersion(context, firCriteriaVersion);
                    // download new criteria version
                    FirebaseStorageHelper.downloadCriteria(context);
                } else {
                    // same version - no need to donwload
                    startCriteria.clear();
                    stoppCriteria.clear();
                    // get scales from SharedPreferences
                    startCriteria.addAll(SharedPreferencesHelper.getStartCriteria(context));
                    stoppCriteria.addAll(SharedPreferencesHelper.getStoppCriteria(context));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ScalesVersion", "Error");
            }
        });


    }

    static int scalesTotal = 12;
    static int scalesCurrent = 0;


    public static double generateScaleResult(GeriatricScaleFirebase scale) {
        double res = 0;
        ArrayList<QuestionFirebase> questionsFromTest = FirebaseDatabaseHelper.getQuestionsFromScale(scale);

        GeriatricScaleNonDB scaleNonDb = Scales.getScaleByName(scale.getScaleName());

        if (scale.getSingleQuestion()) {
            //system.out.println("SINGLE");
            ScoringNonDB scoring = scaleNonDb.getScoring();
            ArrayList<GradingNonDB> valuesBoth = scoring.getValuesBoth();
            for (GradingNonDB grade : valuesBoth) {
                if (grade.getGrade().equals(scale.getAnswer())) {
                    scale.setResult(Double.parseDouble(grade.getScore()));
                    FirebaseDatabaseHelper.updateScale(scale);
                    return Double.parseDouble(grade.getScore());
                }
            }
        } else {
            for (QuestionFirebase question : questionsFromTest) {
                int questionIndex = questionsFromTest.indexOf(question);
                // in the Hamilton scale, only the first 17 questions make up the result
                if (scale.getScaleName().equals(Constants.test_name_hamilton) &&
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

                    ArrayList<ChoiceNonDB> choices = FirebaseDatabaseHelper.getChoicesForQuestion(
                            scaleNonDb.getQuestions().get(questionIndex)
                    );
                    for (ChoiceNonDB c : choices) {
                        if (c.getName().equals(selectedChoice)) {
                            //system.out.println(c.toString());
                            res += c.getScore();
                        }
                    }
                }
            }
        }

        if (scale.getScaleName().equals(Constants.test_name_mini_nutritional_assessment_global)) {
            // check if triagem is already answered
            Log.d("Nutritional", "Global pressed");

            // get scales from this session
//            ArrayList<GeriatricScaleFirebase> allScales = GeriatricScale.getAllScales();

            GeriatricScaleFirebase triagem = FirebaseDatabaseHelper.getScaleFromSession(FirebaseDatabaseHelper.getSessionByID(scale.getSessionID()),
                    Constants.test_name_mini_nutritional_assessment_triagem);
            res += FirebaseHelper.generateScaleResult(triagem);
        }
        if (scale.getScaleName().equals(Constants.test_name_set_set)) {
            // result is the value from the last question (scoring)
            res = questionsFromTest.get(questionsFromTest.size() - 1).getAnswerNumber();
        }
        scale.setResult(res);

        // update scale result
        FirebaseDatabaseHelper.updateScale(scale);

        return res;
    }


    public static void initializeFirebase() {
        /*
        If your app uses Firebase Authentication, the Firebase Realtime Database
         client persists the user's authentication token across app restarts.
          If the auth token expires while your app is offline, the client pauses
           write operations until your app re-authenticates the user, otherwise the
            write operations might fail due to security rules.
         */

        // set patients
        String userArea = "users/" + FirebaseAuth.getInstance().getCurrentUser().getUid();

        PATIENTS = userArea + "/patients";
        SESSIONS = userArea + "/sessions";
        SCALES = userArea + "/scales";
        QUESTIONS = userArea + "/questions";
        PRESCRIPTIONS = userArea + "/prescriptions";
        CHOICES = userArea + "/choices";


        firebaseTablePatients = mFirebaseInstance.getReference(FirebaseHelper.PATIENTS);
        firebaseTableSessions = mFirebaseInstance.getReference(FirebaseHelper.SESSIONS);
        firebaseTableScales = mFirebaseInstance.getReference(FirebaseHelper.SCALES);
        firebaseTableQuestions = mFirebaseInstance.getReference(FirebaseHelper.QUESTIONS);
        firebaseTablePrescriptions = mFirebaseInstance.getReference(FirebaseHelper.PRESCRIPTIONS);
        firebaseTableChoices = mFirebaseInstance.getReference(FirebaseHelper.CHOICES);

        // fetch all data from firebase
        FirebaseDatabaseHelper.fetchPatients();
        FirebaseDatabaseHelper.fetchFavoritePatients();
        FirebaseDatabaseHelper.fetchSessions();
        FirebaseDatabaseHelper.fetchScales();
        FirebaseDatabaseHelper.fetchQuestions();
        FirebaseDatabaseHelper.fetchPrescriptions();
    }

    public static ArrayList<SessionFirebase> getSessions() {
        return sessions;
    }

    public static ArrayList<GeriatricScaleFirebase> getScales() {
        return scales;
    }

    public static ArrayList<QuestionFirebase> getQuestions() {
        return questions;
    }

    public static ArrayList<PrescriptionFirebase> getPrescriptions() {
        return prescriptions;
    }


    static String languageSpanish = "ES";
    static String languageEnglish = "EN";
    static String languagePortuguese = "PT";

    static String[] scalesNames = {
            Constants.test_name_barthel_index,
            Constants.test_name_clock_drawing,
            Constants.test_name_escalaDepressaoYesavage,
            Constants.test_name_escalaLawtonBrody,
            Constants.test_name_marchaHolden,
            Constants.test_name_mini_mental_state,
            Constants.test_name_mini_nutritional_assessment_global,
            Constants.test_name_mini_nutritional_assessment_triagem,
            Constants.test_name_valoracionSocioFamiliar,
            Constants.test_name_tinetti,
            Constants.test_name_testeDeKatz,
            Constants.test_name_recursos_sociales,
    };

    static String[] scalesLanguages = {
            languageSpanish,
            languageEnglish,
            languagePortuguese,
            languagePortuguese,
            languagePortuguese,
            languagePortuguese,
            languagePortuguese,
            languagePortuguese,
            languageSpanish,
            languagePortuguese,
            languagePortuguese,
            languageSpanish
    };

}
