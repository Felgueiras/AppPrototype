package com.felgueiras.apps.geriatric_helper.Firebase;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GradingNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.ScoringNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.Scales;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.DatesHandler;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.SharedPreferencesHelper;
import com.felgueiras.apps.geriatric_helper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


/**
 * Created by felgueiras on 08/04/2017.
 */

public class FirebaseHelper {

    private static final String PUBLIC = "/public";
    private static FirebaseDatabase mFirebaseInstance = null;

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
    private static ArrayList<PatientFirebase> patients = new ArrayList<>();
    /**
     * Favorite Patients.
     */
    private static ArrayList<PatientFirebase> favoritePatients = new ArrayList<>();
    /**
     * Sessions.
     */
    private static ArrayList<SessionFirebase> sessions = new ArrayList<>();
    /**
     * Scales.
     */
    private static ArrayList<GeriatricScaleFirebase> scales = new ArrayList<>();
    /**
     * Questions.
     */
    private static ArrayList<QuestionFirebase> questions = new ArrayList<>();
    /**
     * Choices.
     */
    private static ArrayList<ChoiceFirebase> choices = new ArrayList<>();
    /**
     * Prescriptions.
     */
    private static ArrayList<PrescriptionFirebase> prescriptions = new ArrayList<>();
    public static boolean canLeaveLaunchScreen = false;
    private static FirebaseRemoteConfig mFirebaseRemoteConfig;


    public static ArrayList<PatientFirebase> getPatients() {
        return patients;
    }


    /**
     * Get all patients from Firebase.
     *
     * @return
     */
    public static void fetchPatients() {

//        FirebaseHelper.firebaseTablePatients.orderByChild("name").addValueEventListener(new ValueEventListener() {
        FirebaseHelper.firebaseTablePatients.orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                patients.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    PatientFirebase patient = postSnapshot.getValue(PatientFirebase.class);
                    patient.setKey(postSnapshot.getKey());
                    patients.add(patient);
                }
                Log.d("Fetch", "Patients");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        });
    }

    public static void initializeAndCheckVersions(final Context context) {
        if (Constants.firebaseInstance == null) {
            // allow offline persistence
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            mFirebaseInstance = FirebaseDatabase.getInstance();
            Constants.firebaseInstance = mFirebaseInstance;
        } else {
            mFirebaseInstance = Constants.firebaseInstance;
        }

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
                } else {
                    // same version - no need to donwload
//                    canLeaveLaunchScreen = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ScalesVersion", "Error");
            }
        });
    }

    public static ArrayList<PatientFirebase> getFavoritePatients() {
        return favoritePatients;
    }

    /**
     * Fetch favorite patients from Firebase.
     *
     * @return
     */
    public static void fetchFavoritePatients() {

        FirebaseHelper.firebaseTablePatients.orderByChild("favorite").equalTo(true).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                favoritePatients.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    PatientFirebase patient = postSnapshot.getValue(PatientFirebase.class);
                    patient.setKey(postSnapshot.getKey());
                    favoritePatients.add(patient);
//                    Log.d("Firebase", "Patients favorite: " + favoritePatients.size());
                    Log.d("Fetch", "Favorite patients");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        });
    }


    public static void getPatient() {
        // get reference to 'users' node

        firebaseTablePatients.orderByChild("name").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PatientFirebase appTitle = dataSnapshot.getValue(PatientFirebase.class);
                Log.d("Patient", appTitle.getName());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        firebaseTablePatients.addListenerForSingleValueEvent(
//                new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        //Get map of users in datasnapshot
//                        collectPhoneNumbers((Map<String, Object>) dataSnapshot.getValue());
//                    }
//
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        //handle databaseError
//                    }
//                });

        // get all Patients
        firebaseTablePatients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    PatientFirebase patient = postSnapshot.getValue(PatientFirebase.class);
                    patients.add(patient);
                }
                Log.d("Patient", "Size: " + patients.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        });


    }


    static int scalesTotal = 12;
    static int scalesCurrent = 0;

    public static int getScalesTotal() {
        return scalesTotal;
    }

    public static void setScalesTotal(int scalesTotal) {
        FirebaseHelper.scalesTotal = scalesTotal;
    }

    public static int getScalesCurrent() {
        return scalesCurrent;
    }

    public static void setScalesCurrent(int scalesCurrent) {
        FirebaseHelper.scalesCurrent = scalesCurrent;
    }

    /**
     * Download all scales.
     */
    public static void downloadScales() {

        // get system language
//        final String scaleLanguage = Locale.getDefault().getLanguage().toUpperCase();


        // download scales from that language
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).setPrettyPrinting();
        final Gson gson = builder.create();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        // clear the scales
        Scales.scales.clear();
        scalesCurrent = 0;

        for (int i = 0; i < scalesNames.length; i++) {
            final String scaleName = scalesNames[i];
            final String scaleLanguage = scalesLanguages[i];
            String fileName = scaleName + "-" + scaleLanguage + ".json";


            StorageReference storageRef = storage.getReferenceFromUrl(firebaseURL).child("scales/" + fileName);

            try {
                final File localFile = File.createTempFile("scale", "json");
                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        try {
                            GeriatricScaleNonDB scaleNonDB = gson.fromJson(new FileReader(localFile), GeriatricScaleNonDB.class);
                            Scales.scales.add(scaleNonDB);
                            scalesCurrent++;
                            if (scalesCurrent == scalesTotal)
                                canLeaveLaunchScreen = true;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        if (exception instanceof com.google.firebase.storage.StorageException) {
                            // scale was not found for that language
                            Log.d("Download", "Scale " + scaleName + " does not exist for " + scaleLanguage + " language");
                        }
                        scalesCurrent++;
                        if (scalesCurrent == scalesTotal)
                            canLeaveLaunchScreen = true;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }


    /**
     * Get sessions from patient.
     *
     * @param patient
     * @return
     */
    public static ArrayList<SessionFirebase> getSessionsFromPatient(PatientFirebase patient) {
        ArrayList<String> sessionsIDS = patient.getSessionsIDS();
        final ArrayList<SessionFirebase> sessions = new ArrayList<>();

        for (int i = 0; i < sessionsIDS.size(); i++) {
            String currentID = sessionsIDS.get(i);
            sessions.add(FirebaseHelper.getSessionByID(currentID));
        }
        return sessions;
    }

    /**
     * Get prescription from patient.
     *
     * @param patient
     * @return
     */
    public static ArrayList<PrescriptionFirebase> getPrescriptionsFromPatient(PatientFirebase patient) {
        ArrayList<String> prescriptionsIDS = patient.getPrescriptionsIDS();
        final ArrayList<PrescriptionFirebase> prescriptionsForPatient = new ArrayList<>();

        for (int i = 0; i < prescriptionsIDS.size(); i++) {
            String currentID = prescriptionsIDS.get(i);
            prescriptionsForPatient.add(FirebaseHelper.getPrescriptionByID(currentID));
        }
        return prescriptionsForPatient;
    }

    public static ChoiceFirebase getChoiceByID(String choiceID) {

        ArrayList<ChoiceFirebase> choicesToConsider = new ArrayList<>();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            choicesToConsider = choices;
        } else {
            choicesToConsider = Constants.publicChoices;

        }
        for (ChoiceFirebase choice : choicesToConsider) {
            if (choice.getGuid().equals(choiceID))
                return choice;
        }
        return null;
    }


    /**
     * Get a scale by its ID.
     *
     * @param scaleID
     * @return
     */
    public static GeriatricScaleFirebase getScaleByID(String scaleID) {

        for (GeriatricScaleFirebase scale : scales) {
            if (scale.getGuid().equals(scaleID))
                return scale;
        }
        return null;
    }

    public static SessionFirebase getSessionByID(String sessionID) {

        for (SessionFirebase session : sessions) {
            if (session.getGuid().equals(sessionID))
                return session;
        }
        return null;
    }

    public static PrescriptionFirebase getPrescriptionByID(String prescriptionID) {

        for (PrescriptionFirebase prescription : prescriptions) {
            if (prescription.getGuid().equals(prescriptionID))
                return prescription;
        }
        return null;
    }

    /**
     * Get a Question by its ID.
     *
     * @param questionID
     * @return
     */
    public static QuestionFirebase getQuestionByID(String questionID) {

        ArrayList<QuestionFirebase> questionsToConsider = new ArrayList<>();
        // get scales with those IDS
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            questionsToConsider = questions;
        } else {
            questionsToConsider = Constants.publicQuestions;

        }
        for (QuestionFirebase question : questionsToConsider) {
            if (question.getGuid().equals(questionID))
                return question;
        }
        return null;
    }


    /**
     * Get a patient by its ID.
     *
     * @return
     */
    @Nullable
    public static PatientFirebase getPatientFromSession(SessionFirebase session) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            return null;
        }
        if (session.getPatientID() == null) {
            return null;
        }
        for (PatientFirebase patient : patients) {
            if (patient.getGuid().equals(session.getPatientID()))
                return patient;
        }
        return null;
    }

    /**
     * Fetch Sessions from Firebase.
     */
    public static void fetchSessions() {
        FirebaseHelper.firebaseTableSessions.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sessions.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    SessionFirebase session = postSnapshot.getValue(SessionFirebase.class);
                    session.setKey(postSnapshot.getKey());
                    sessions.add(session);
                }
                Log.d("Fetch", "Sessions");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        });
    }

    /**
     * Fetch Scales from Firebase.
     */
    public static void fetchScales() {
        FirebaseHelper.firebaseTableScales.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                scales.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    GeriatricScaleFirebase scale = postSnapshot.getValue(GeriatricScaleFirebase.class);
                    scale.setKey(postSnapshot.getKey());
                    scales.add(scale);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        });
    }

    /**
     * Fetch questions from Firebase.
     */
    public static void fetchQuestions() {
        FirebaseHelper.firebaseTableQuestions.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                questions.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    QuestionFirebase question = postSnapshot.getValue(QuestionFirebase.class);
                    question.setKey(postSnapshot.getKey());
                    questions.add(question);
                }
                Log.d("Fetch", "Questions");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        });
    }

    public static void fetchChoices() {
        FirebaseHelper.firebaseTableChoices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                choices.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ChoiceFirebase choice = postSnapshot.getValue(ChoiceFirebase.class);
                    choice.setKey(postSnapshot.getKey());
                    choices.add(choice);
                }
                Log.d("Fetch", "Choices");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        });
    }

    /**
     * Fetch prescriptions from Firebase.
     */
    public static void fetchPrescriptions() {
        FirebaseHelper.firebaseTablePrescriptions.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                prescriptions.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    PrescriptionFirebase prescription = postSnapshot.getValue(PrescriptionFirebase.class);
                    prescription.setKey(postSnapshot.getKey());
                    prescriptions.add(prescription);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        });
    }

    /**
     * Get a scale from a session by its name.
     *
     * @param session
     * @param scaleName
     * @return
     */
    public static GeriatricScaleFirebase getScaleFromSession(SessionFirebase session, String scaleName) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            ArrayList<String> scalesIDS = session.getScalesIDS();
            // get scales with those IDS

            for (GeriatricScaleFirebase scale : scales) {
                if (scalesIDS.contains(scale.getGuid()) && scale.getScaleName().equals(scaleName))
                    return scale;
            }
            return null;
        } else {
            // public session
            for (GeriatricScaleFirebase scale : Constants.publicScales) {
                if (scale.getScaleName().equals(scaleName)) {
                    return scale;
                }
            }
        }

        return null;
    }

    public static ArrayList<GeriatricScaleFirebase> getScalesFromSession(SessionFirebase session) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {

            ArrayList<String> scalesIDS = session.getScalesIDS();
            ArrayList<GeriatricScaleFirebase> scalesForSession = new ArrayList<>();
            // get scales with those IDS

            for (GeriatricScaleFirebase scale : scales) {
                if (scalesIDS.contains(scale.getGuid()))
                    scalesForSession.add(scale);
            }
            return scalesForSession;

        } else {
            return Constants.publicScales;
        }

    }

    public static SessionFirebase getSessionFromScale(GeriatricScaleFirebase scale) {
        ArrayList<GeriatricScaleFirebase> scalesForSession = new ArrayList<>();
        // get scales with those IDS

        for (SessionFirebase session : sessions) {
            if (session.getGuid().equals(scale.getSessionID())) {
                return session;
            }
        }
        return null;

    }


    /**
     * Get patient associated with a prescription.
     *
     * @param prescription
     * @return
     */
    public static PatientFirebase getPatientFromPrescription(PrescriptionFirebase prescription) {

        for (PatientFirebase patient : patients) {
            if (patient.getGuid().equals(prescription.getPatientID())) {
                return patient;
            }
        }
        return null;

    }


    /**
     * Get questions from a scale.
     *
     * @param scale
     * @return
     */
    public static ArrayList<QuestionFirebase> getQuestionsFromScale(GeriatricScaleFirebase scale) {
        ArrayList<QuestionFirebase> questionsFromScale = new ArrayList<>();
        ArrayList<QuestionFirebase> questionsToConsider = new ArrayList<>();
        // get scales with those IDS
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            questionsToConsider = questions;
        } else {
            questionsToConsider = Constants.publicQuestions;

        }

        for (QuestionFirebase question : questionsToConsider) {
            if (question.getScaleID().equals(scale.getGuid())) {
                questionsFromScale.add(question);
            }
        }
        return questionsFromScale;
    }


    /**
     * Get Choices for a Question.
     *
     * @param question
     * @return
     */
    public static ArrayList<ChoiceFirebase> getChoicesForQuestion(QuestionFirebase question) {
        ArrayList<ChoiceFirebase> choicesForQuestion = new ArrayList<>();
        ArrayList<ChoiceFirebase> choicesToConsider = new ArrayList<>();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            choicesToConsider = choices;
        } else {
            choicesToConsider = Constants.publicChoices;

        }

        for (ChoiceFirebase choice : choicesToConsider) {
            if (choice.getQuestionID().equals(question.getGuid())) {
                choicesForQuestion.add(choice);
            }
        }
        return choicesForQuestion;
    }

    public static double generateScaleResult(GeriatricScaleFirebase scale) {
        double res = 0;
        ArrayList<QuestionFirebase> questionsFromTest = getQuestionsFromScale(scale);

        if (scale.getSingleQuestion()) {
            //system.out.println("SINGLE");
            ScoringNonDB scoring = Scales.getScaleByName(scale.getScaleName()).getScoring();
            ArrayList<GradingNonDB> valuesBoth = scoring.getValuesBoth();
            for (GradingNonDB grade : valuesBoth) {
                if (grade.getGrade().equals(scale.getAnswer())) {
                    scale.setResult(Double.parseDouble(grade.getScore()));
                    updateScale(scale);
                    return Double.parseDouble(grade.getScore());
                }
            }
        } else {
            for (QuestionFirebase question : questionsFromTest) {
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
                    ArrayList<ChoiceFirebase> choices = getChoicesForQuestion(question);
                    for (ChoiceFirebase c : choices) {
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

            GeriatricScaleFirebase triagem = FirebaseHelper.getScaleFromSession(getSessionByID(scale.getSessionID()),
                    Constants.test_name_mini_nutritional_assessment_triagem);
            res += FirebaseHelper.generateScaleResult(triagem);
        }
        if (scale.getScaleName().equals(Constants.test_name_set_set)) {
            // result is the value from the last question (scoring)
            res = questionsFromTest.get(questionsFromTest.size() - 1).getAnswerNumber();
        }
        scale.setResult(res);

        // update scale result
        updateScale(scale);

        return res;
    }

    /**
     * Save a Patient.
     *
     * @param patient
     */
    public static void createPatient(PatientFirebase patient) {
        String patientID = FirebaseHelper.firebaseTablePatients.push().getKey();
        patient.setKey(patientID);
        FirebaseHelper.firebaseTablePatients.child(patientID).setValue(patient);
    }


    public static void createPrescription(PrescriptionFirebase prescription) {
        String prescriptionID = FirebaseHelper.firebaseTablePrescriptions.push().getKey();
        prescription.setKey(prescriptionID);
        FirebaseHelper.firebaseTablePrescriptions.child(prescriptionID).setValue(prescription);
    }

    /**
     * Save a Session.
     *
     * @param session
     */
    public static void createSession(SessionFirebase session) {
        String sessionID = FirebaseHelper.firebaseTableSessions.push().getKey();
        session.setKey(sessionID);
        FirebaseHelper.firebaseTableSessions.child(sessionID).setValue(session);
    }

    /**
     * Save a Question.
     *
     * @param question
     */
    public static void createQuestion(QuestionFirebase question) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            String questionID = FirebaseHelper.firebaseTableQuestions.push().getKey();
            question.setKey(questionID);
            FirebaseHelper.firebaseTableQuestions.child(questionID).setValue(question);
        } else {
            Constants.publicQuestions.add(question);

        }
    }

    /**
     * Save a Choice.
     *
     * @param choice
     */
    public static void createChoice(ChoiceFirebase choice) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // add to questions
//        QuestionFirebase question = getQuestionByID(choice.getQuestionID());
//        question.getChoicesIDs().add(choice.getGuid());
//        updateQuestion(question);

            // add to table
            String choiceID = FirebaseHelper.firebaseTableChoices.push().getKey();
            FirebaseHelper.firebaseTableChoices.child(choiceID).setValue(choice);
        } else {
            Constants.publicChoices.add(choice);
        }
    }

    /**
     * Create a Scale.
     *
     * @param scale
     */
    public static void createScale(GeriatricScaleFirebase scale) {
        String scaleID = FirebaseHelper.firebaseTableScales.push().getKey();
        FirebaseHelper.firebaseTableScales.child(scaleID).setValue(scale);
    }

    /**
     * Update scale.
     *
     * @param currentScale
     */
    public static void updateScale(GeriatricScaleFirebase currentScale) {
        // check if logged in
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            FirebaseHelper.firebaseTableScales.child(currentScale.getKey()).setValue(currentScale);
        }
    }

    public static void updateQuestion(QuestionFirebase question) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            FirebaseHelper.firebaseTableQuestions.child(question.getKey()).setValue(question);
        }
    }

    public static void updatePatient(PatientFirebase patient) {
        FirebaseHelper.firebaseTablePatients.child(patient.getKey()).setValue(patient);
    }

    public static void updateSession(SessionFirebase session) {
        if (session != null)
            FirebaseHelper.firebaseTableSessions.child(session.getKey()).setValue(session);
    }

    public static ArrayList<GeriatricScaleFirebase> getScalesForArea(List<GeriatricScaleFirebase> scales, String area) {
        ArrayList<GeriatricScaleFirebase> scalesForArea = new ArrayList<>();
        for (GeriatricScaleFirebase scale : scales) {
            if (Scales.getScaleByName(scale.getScaleName()).getArea().equals(area)) {
                scalesForArea.add(scale);
            }
        }
        return scalesForArea;
    }

    public static void deleteSession(SessionFirebase session) {

        sessions.remove(session);
        // remove session from patient's sessions list (if patient not null)
        PatientFirebase patient = FirebaseHelper.getPatientFromSession(session);
        if (patient != null) {
            patient.getSessionsIDS().remove(session.getGuid());
            updatePatient(patient);
        }

        // delete scales
        ArrayList<GeriatricScaleFirebase> scales = getScalesFromSession(session);
        for (GeriatricScaleFirebase scale : scales) {
            deleteScale(scale);
        }

        firebaseTableSessions.child(session.getKey()).removeValue();
    }


    public static void deletePatient(PatientFirebase patient) {
        // delete sessions from this patient
        ArrayList<SessionFirebase> sessions = FirebaseHelper.getSessionsFromPatient(patient);
        for (SessionFirebase session : sessions) {
            deleteSession(session);
        }

        // delete prescriptions from this patient
        ArrayList<PrescriptionFirebase> prescriptions = FirebaseHelper.getPrescriptionsFromPatient(patient);
        for (PrescriptionFirebase prescription : prescriptions) {
            deletePrescription(prescription);
        }

        firebaseTablePatients.child(patient.getKey()).removeValue();
    }

    /**
     * Erase uncompleted scales from a session.
     *
     * @param session
     */
    public static void eraseScalesNotCompleted(SessionFirebase session) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // logged in - erase from Firebase
            List<GeriatricScaleFirebase> scales = getScalesFromSession(session);
            for (GeriatricScaleFirebase scale : scales) {
                if (!scale.isCompleted()) {
                    deleteScale(scale);
                }
            }
        } else {
            // not logged in - erase from Constants
            ArrayList<GeriatricScaleFirebase> completedScales = new ArrayList<>();
            for (GeriatricScaleFirebase scale : Constants.publicScales) {
                if (scale.isCompleted()) {
                    completedScales.add(scale);
                }
            }
            Constants.publicScales = completedScales;
        }
    }

    /**
     * Delete a scale from Firebase.
     *
     * @param scale
     */
    public static void deleteScale(GeriatricScaleFirebase scale) {

        // remove from the session's list of scales IDs
        SessionFirebase session = FirebaseHelper.getSessionFromScale(scale);
        if (session != null) {
            session.getScalesIDS().remove(scale.getGuid());
            updateSession(session);
        }

        // delete questions
        ArrayList<QuestionFirebase> questions = getQuestionsFromScale(scale);
        for (QuestionFirebase question : questions) {
            deleteChoice(question);
        }

        // remove associated images or videos
        if (scale.isContainsPhoto()) {
            Log.d("Firebase", "Removing photo");
            FirebaseStorage storage = FirebaseStorage.getInstance();
            // photo reference
            StorageReference storageRef = storage.getReferenceFromUrl("gs://appprototype-bdd27.appspot.com")
                    .child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/images/" + scale.getPhotoPath());

            // Delete the file
            storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                    Log.d("Firebase", "Photo deleted!");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                }
            });
        }
        if (scale.isContainsVideo()) {

        }

        // remove scale
        firebaseTableScales.child(scale.getKey()).removeValue();
    }


    public static void deleteChoice(QuestionFirebase choide) {

        // remove choice
        firebaseTableChoices.child(choide.getKey()).removeValue();
    }


    /**
     * Delete a prescription.
     *
     * @param prescription
     */
    public static void deletePrescription(PrescriptionFirebase prescription) {
        // remove from patient's list of prescriptions
        PatientFirebase patient = FirebaseHelper.getPatientFromPrescription(prescription);
        patient.getPrescriptionsIDS().remove(prescription.getGuid());
        updatePatient(patient);

        // remove prescription
        firebaseTablePrescriptions.child(prescription.getKey()).removeValue();
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
        FirebaseHelper.fetchPatients();
        FirebaseHelper.fetchFavoritePatients();
        FirebaseHelper.fetchSessions();
        FirebaseHelper.fetchScales();
        FirebaseHelper.fetchQuestions();
        FirebaseHelper.fetchPrescriptions();
        FirebaseHelper.fetchChoices();
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

    public static ArrayList<ChoiceFirebase> getChoices() {
        return choices;
    }

    public static ArrayList<PrescriptionFirebase> getPrescriptions() {
        return prescriptions;
    }

    /**
     * Get different session dates (display sessions by date).
     *
     * @return
     */
    public static ArrayList<Date> getDifferentSessionDates() {
        HashSet<Date> days = new HashSet<>();
        for (SessionFirebase session : sessions) {
            Date dateWithoutHour = DatesHandler.getDateWithoutHour(session.getDate());
            days.add(dateWithoutHour);
        }
        ArrayList<Date> differentDates = new ArrayList<>();
        differentDates.addAll(days);
        // order by date (descending)
        Collections.sort(differentDates, new Comparator<Date>() {
            @Override
            public int compare(Date first, Date second) {
                if (first.after(second)) {
                    return -1;
                } else if (first.before(second)) {
                    return 1;
                } else
                    return 0;
            }
        });
        return differentDates;

    }

    /**
     * Get sessions from a date.
     *
     * @param firstDay
     * @return
     */
    public static List<SessionFirebase> getSessionsFromDate(Date firstDay) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(firstDay.getTime());

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        // first day
        firstDay = cal.getTime();
        // second day
        cal.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        Date secondDay = cal.getTime();

        //system.out.println("Getting sessions from " + firstDay + "-" + secondDay);
        // TODO get evaluations from that date
//        return new Select()
//                .from(Session.class)
//                .where("date > ? and date < ?", firstDay.getTime(), secondDay.getTime())
//                .orderBy("guid ASC")
//                .execute();
        return sessions;
    }

    private static Gson gson;


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

    public static ArrayList<GeriatricScaleFirebase> getScaleInstancesForPatient(ArrayList<SessionFirebase> patientSessions, String scaleName) {
        ArrayList<GeriatricScaleFirebase> scaleInstances = new ArrayList<>();
        // get instances for that test
        for (SessionFirebase currentSession : patientSessions) {
            List<GeriatricScaleFirebase> scalesFromSession = FirebaseHelper.getScalesFromSession(currentSession);
            for (GeriatricScaleFirebase currentScale : scalesFromSession) {
                if (currentScale.getScaleName().equals(scaleName)) {
                    scaleInstances.add(currentScale);
                }
            }
        }
        return scaleInstances;
    }

    /**
     * JSON handling.
     */
    public static void uploadScales() {

        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).setPrettyPrinting();
        gson = builder.create();


        for (int i = 0; i < scalesNames.length; i++) {
            // scale name
            String scaleName = scalesNames[i];
            String scaleLanguage = scalesLanguages[i];
            String jsonArray = gson.toJson(Scales.getScaleByName(scaleName));

            String fileName = scaleName + "-" + scaleLanguage + ".json";
            // upload file
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.
                    getReferenceFromUrl("gs://appprototype-bdd27.appspot.com")
                    .child("scales/" + fileName);

            UploadTask uploadTask = storageReference.putBytes(jsonArray.getBytes());
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                }
            });

        }


    }

    /**
     * Setup remote config (Firebase).
     *
     * @param context
     */
    public static void setupRemoteConfig(Context context) {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(remoteConfigSettings);

        // set defaults - can be setup using a map
//        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        // fetch remote config
        // cache expiration in seconds
        long cacheExpiration = 3600;

//expire the cache immediately for development mode.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        // fetch
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            // task successful. Activate the fetched data
                            mFirebaseRemoteConfig.activateFetched();

                            // check whether promo is on
                            String appName = mFirebaseRemoteConfig.getString("app_name");
                            Log.d("RemoteConfig", appName);

                        } else {
                            //task failed
                        }
                    }
                });
    }

    /**
     * Get a String for a "key" using Firebase Remote Config instance.
     *
     * @param key
     * @param stringInResources
     * @return
     */
    public static String getString(String key, String stringInResources) {
        String ret = "";
        String stringFromFirebase = mFirebaseRemoteConfig.getString(key);
        if (stringFromFirebase.equals("")) {
            // if not defined in RemoteConfig, load from languages file
            ret = stringInResources;
        } else {
            ret = stringFromFirebase;
        }
        return ret.replace("\\n", System.getProperty("line.separator"));


    }
}
