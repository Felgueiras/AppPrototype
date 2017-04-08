package com.felgueiras.apps.geriatric_helper;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.felgueiras.apps.geriatric_helper.DataTypes.DB.Choice;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GradingNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.ScoringNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.Scales;
import com.felgueiras.apps.geriatric_helper.Firebase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.QuestionFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.DatesHandler;
import com.felgueiras.apps.geriatric_helper.Patients.AllPatients.PatientCardPatientsList;
import com.felgueiras.apps.geriatric_helper.Patients.Favorite.PatientsFavoriteFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Random;


/**
 * Created by felgueiras on 08/04/2017.
 */

public class FirebaseHelper {

    /**
     * Firebase realtime database URL.
     */
    private static final String firebaseURL = "gs://appprototype-bdd27.appspot.com";

    /**
     * Patients table name.
     */
    private static final String PATIENTS = "patients";
    /**
     * Sessions table name.
     */
    private static final String SESSIONS = "sessions";

    /**
     * Questions table name.
     */
    private static final String QUESTIONS = "questions";


    private static FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();


    /**
     * Firebase - patients table.
     */
    public static DatabaseReference firebaseTablePatients = mFirebaseInstance.getReference(FirebaseHelper.PATIENTS);
    /**
     * Firebase - sessions table.
     */
    public static DatabaseReference firebaseTableSessions = mFirebaseInstance.getReference(FirebaseHelper.SESSIONS);
    /**
     * Firebase - scales table.
     */
    public static DatabaseReference firebaseTableScales = mFirebaseInstance.getReference(FirebaseHelper.SESSIONS);
    /**
     * Firebase - questions table.
     */
    public static DatabaseReference firebaseTableQuestions = mFirebaseInstance.getReference(FirebaseHelper.QUESTIONS);
    private static SessionFirebase session;
    private static PatientFirebase patient;

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

    public static void createPatient() {


        Log.d("Dummy", "Inserting dummy data");

        int totalPatients = 30;
        String[] patientsMale =
                {"Manuel Vieira",
                        "Ivo Silva",
                        "Júlio Arrábida",
                        "Manuel João Costa",
                        "Vitor Manuel da Assunção",
                        "José Pinto",
                        "Teotónio Francisco",
                        "Fernando Silva",
                        "Augusto Cunha",
                        "Óscar Felgueiras",
                        "Vítor de Sá",
                        "Leandro Ricardo",
                        "Mário Alexandre Costa",
                        "Aníbal Salgueiro",
                        "Bernardo Silva"};
        String[] patientsFemale =
                {"Maria da Graça",
                        "Maria Silva",
                        "Júlia Adalberto",
                        "Beatriz Fernandes Madureira",
                        "Emília Felgueiras Pinto",
                        "Maria Adélia Cirne",
                        "Ana Inês Gonçalves",
                        "Clara Ferreira",
                        "Ana Maria Pureza",
                        "Leonor Fernandes",
                        "Lurdes Onofre Seixas",
                        "Teresa Silves",
                        "Paula Faria",
                        "Leonor da Graça Gomes",
                        "Beatriz Flores"};

        String[] patientNames = new String[totalPatients];
        int[] patientAges = new int[totalPatients];
        String[] patientAddresses = new String[totalPatients];
        double[] patientGenders = new double[totalPatients];
        Date[] patientBirthDates = new Date[totalPatients];


        String[] addresses = {
                "Aveiro",
                "Eixo",
                "Esgueira",
                "Ílhavo",
                "Estarreja",
                "Cacia",
                "Azurva",
                "Salreu"
        };

        Random random = new Random();

        /**
         * Male patients.
         */
        for (String aPatientsMale : patientsMale) {
            // create patients
            PatientFirebase patient = new PatientFirebase();
            patient.setName(aPatientsMale);
            patient.setProcessNumber(random.nextInt(1000) + "");
            patient.setBirthDate(DatesHandler.stringToDate("01-12-1920"));
            patient.setGuid("PATIENT-" + aPatientsMale);
            patient.setAddress(addresses[random.nextInt(addresses.length)]);
            patient.setPicture(R.drawable.male);
            patient.setGender(Constants.MALE);
            patient.setFavorite(false);
//            patient.save();
            String patientID = firebaseTablePatients.push().getKey();
            firebaseTablePatients.child(patientID).setValue(patient);
        }
//        /**
//         * Female patients.
//         */
//        for (String aPatientsFemale : patientsFemale) {
//            // create patients
//            Patient patient = new Patient();
//            patient.setName(aPatientsFemale);
//            patient.setProcessNumber(random.nextInt(1000) + "");
//            patient.setBirthDate(DatesHandler.stringToDate("01-12-1920"));
//            patient.setGuid("PATIENT-" + aPatientsFemale);
//            patient.setAddress(addresses[random.nextInt(addresses.length)]);
//            patient.setPicture(R.drawable.female);
//            patient.setGender(Constants.FEMALE);
//            patient.setFavorite(false);
//
//        }
    }


    public static void loadFirebaseFile() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://appprototype-bdd27.appspot.com").child("sample.jpg");

        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//                    mImageView.setImageBitmap(bitmap);
                    Log.d("Storage", "success");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e) {
        }

    }


    public static ArrayList<PatientFirebase> getPatients() {
        return patients;
    }


    /**
     * Get all patients from Firebase.
     *
     * @return
     */
    public static void fetchPatients() {
        FirebaseHelper.firebaseTablePatients.orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                patients.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    PatientFirebase patient = postSnapshot.getValue(PatientFirebase.class);
                    patient.setKey(postSnapshot.getKey());
                    patients.add(patient);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
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
                    Log.d("Firebase", "Patients favorite: " + favoritePatients.size());
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

    private static void collectPhoneNumbers(Map<String, Object> patients) {
        ArrayList<String> names = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : patients.entrySet()) {

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            names.add((String) singleUser.get("name"));
        }
    }


    public static void readScaleFromFirebase(String scaleName) {

        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).setPrettyPrinting();
        final Gson gson = builder.create();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(firebaseURL).child("scales/" + scaleName + ".json");

        try {
            final File localFile = File.createTempFile("scale", "json");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    try {
                        GeriatricScaleNonDB scaleNonDB = gson.fromJson(new FileReader(localFile), GeriatricScaleNonDB.class);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static ArrayList<SessionFirebase> getSessionsFromPatient(PatientFirebase patient) {
        final ArrayList<SessionFirebase> sessions = new ArrayList<>();
        {
            for (int i = 0; i < patient.getSessionsIDS().size(); i++) {
                String currentID = patient.getSessionsIDS().get(i);

                firebaseTableSessions.orderByChild("guid").equalTo(currentID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        sessions.add(dataSnapshot.getValue(SessionFirebase.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            return sessions;
        }
    }


    public static SessionFirebase getSessionByID(String sessionID) {

        firebaseTableSessions.orderByChild("guid").equalTo(sessionID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                session = dataSnapshot.getValue(SessionFirebase.class);
                session.setKey(dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return session;
    }


    /**
     * Get a patient by its ID.
     *
     * @return
     */
    @Nullable
    public static PatientFirebase getPatientFromSession(SessionFirebase session) {
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
     * Get a scale from a session by its name.
     *
     * @param session
     * @param scaleName
     * @return
     */
    public static GeriatricScaleFirebase getScaleFromSession(SessionFirebase session, String scaleName) {
        ArrayList<String> scalesIDS = session.getScalesIDS();
        // get scales with those IDS

        for (GeriatricScaleFirebase scale : scales) {
            if (scalesIDS.contains(scale.getGuid()) &&
                    scale.getScaleName().equals(scaleName))
                return scale;
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
        ArrayList<String> questionsIDs = scale.getQuestionsIDs();
        ArrayList<QuestionFirebase> questionsFromScale = new ArrayList<>();
        // get scales with those IDS

        for (QuestionFirebase question : questions) {
            if (question.getScaleID().equals(scale.getGuid())) {
                questionsFromScale.add(question);
            }
        }
        return questionsFromScale;
    }

    public double generateScaleResult(GeriatricScaleFirebase scale) {
        double res = 0;
        // TODO get questions from scale
        ArrayList<QuestionFirebase> questionsFromTest = getQuestionsFromScale(scale);

        if (scale.getSingleQuestion()) {
            //system.out.println("SINGLE");
            ScoringNonDB scoring = Scales.getScaleByName(scale.getScaleName()).getScoring();
            ArrayList<GradingNonDB> valuesBoth = scoring.getValuesBoth();
            for (GradingNonDB grade : valuesBoth) {
                if (grade.getGrade().equals(scale.getAnswer())) {
                    scale.setResult(Double.parseDouble(grade.getScore()));
                    // TODO save
//                    this.save();
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
}
