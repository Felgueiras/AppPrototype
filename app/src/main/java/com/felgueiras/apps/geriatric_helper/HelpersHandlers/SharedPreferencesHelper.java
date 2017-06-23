package com.felgueiras.apps.geriatric_helper.HelpersHandlers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.Criteria.StartCriteria;
import com.felgueiras.apps.geriatric_helper.DataTypes.Criteria.StoppCriteria;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.Patient;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.Session;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseStorageHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by felgueiras on 21/02/2017.
 */

public class SharedPreferencesHelper {


    public static String isThereOngoingPublicSession(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        return sharedPreferences.getString(context.getResources().getString(R.string.saved_session_public), null);
    }

    public static String isThereOngoingPrivateSession(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        return sharedPreferences.getString(context.getResources().getString(R.string.saved_session_private), null);
    }

    public static void resetPublicSession(Activity context, String sessionID) {
        if (sessionID != null) {
            Session session = Session.getSessionByID(sessionID);
            if (session != null)
                session.delete();
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        sharedPreferences.edit().putString(context.getString(R.string.saved_session_public), null).apply();
    }


    /**
     * Persiste scales version.
     *
     * @param context
     * @param scalesVersion
     */
    public static void setScalesVersion(Context context, int scalesVersion) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        sharedPreferences.edit().putInt(context.getString(R.string.scalesVersion), scalesVersion).apply();
    }

    public static void setCriteriaVersion(Context context, int criteriaVersion) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        sharedPreferences.edit().putInt(context.getString(R.string.criteriaVersion), criteriaVersion).apply();
    }


    /**
     * Get the current scales version.
     *
     * @param context
     * @return
     */
    public static int getScalesVersion(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        return sharedPreferences.getInt(context.getString(R.string.scalesVersion), -1);
    }

    public static int getCriteriaVersion(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        return sharedPreferences.getInt(context.getString(R.string.criteriaVersion), -1);
    }

    public static void setPrivateSession(Activity context, String sessionID) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        sharedPreferences.edit().putString(context.getString(R.string.saved_session_private), sessionID).apply();
    }

    public static void resetPrivateSession(Activity context, String sessionID) {
        Session session = Session.getSessionByID(sessionID);
        if (session != null)
            session.delete();
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        sharedPreferences.edit().putString(context.getString(R.string.saved_session_private), null).apply();
    }

    public static boolean getLockStatus(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        return sharedPreferences.getBoolean("LOCK", false);
    }

    public static void setLockStatus(Context context, boolean lock) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("LOCK", lock).apply();
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        return sharedPreferences.getBoolean(Constants.logged_in, false);
    }


    public static String getUserName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        return sharedPreferences.getString(context.getString(R.string.username), "");
    }

    /**
     * Get user email.
     *
     * @param context
     * @return
     */
    public static String getUserEmail(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        return sharedPreferences.getString(context.getString(R.string.email), "");
    }

    public static String getUserPassword(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        return sharedPreferences.getString(context.getString(R.string.password), "");
    }

    /**
     * Check if it is the first public evaluation.
     *
     * @param context
     * @return
     */
    public static boolean checkFirstPublicEvaluation(Activity context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        boolean alreadyLogged = sharedPreferences.getBoolean(Constants.logged_in, false);
        if (!alreadyLogged) {
            boolean firstPublicEvaluation = sharedPreferences.getBoolean("first_public_evaluation", true);
            if (firstPublicEvaluation) {
                SharedPreferences.Editor e = sharedPreferences.edit();
                //  Edit preference to make it false because we don'checkFirstStart want this to run again
                e.putBoolean("first_public_evaluation", false);
                e.apply();
                return true;
            }
        }
        return false;
    }

    public static void lockSessionCreation(Activity context) {
        // set the user as being logged in
        Log.d("Stack", "locking");

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(Constants.create_session_permitted, false).apply();
    }

    public static void unlockSessionCreation(Activity context) {
        Log.d("Stack", "unlocking");
        // set the user as being logged in
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(Constants.create_session_permitted, true).apply();
    }

    public static boolean isSessionCreationPermitted(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        return sharedPreferences.getBoolean(Constants.create_session_permitted, true);
    }

    /**
     * Add a new patient to SharedPreferences.
     *
     * @param patient
     * @param context
     */
    public static void addPatient(PatientFirebase patient, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);

        // save scale as JSON String
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).setPrettyPrinting();
        Gson gson = builder.create();

        String json = gson.toJson(patient);

        //Set the values
        Set<String> set = new HashSet<>();
        set.addAll(getPatientsSet(context));
        // add new set
        set.add(json);

        sharedPreferences.edit().putStringSet("patients", set).apply();
        Log.d("Patients", "Size: " + set.size());
    }

    /**
     * Remove a patient.
     *
     * @param patientToRemove
     * @param context
     */
    public static void removePatient(PatientFirebase patientToRemove, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);

        // save scale as JSON String
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).setPrettyPrinting();
        Gson gson = builder.create();

        String json = gson.toJson(patientToRemove);

        // get all patients
        ArrayList<PatientFirebase> patients = new ArrayList<>();
        Set<String> patientsSet = getPatientsSet(context);

        Set<String> setWithoutRemovedPatient = new HashSet<>();

        // rebuild patients set
        for (String currentPatientString : new ArrayList<>(patientsSet)) {
            PatientFirebase p = gson.fromJson(currentPatientString, PatientFirebase.class);
            if (!p.getGuid().equals(patientToRemove.getGuid()))
                setWithoutRemovedPatient.add(currentPatientString);
        }

        sharedPreferences.edit().putStringSet("patients", setWithoutRemovedPatient).apply();
        Log.d("Patients", "Size: " + setWithoutRemovedPatient.size());
    }


    /**
     * Persist a scale into shared prferences.
     *
     * @param scaleNonDB
     * @param context
     */
    public static void addScale(GeriatricScaleNonDB scaleNonDB, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);

        // save scale as JSON String
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).setPrettyPrinting();
        Gson gson = builder.create();

        String json = gson.toJson(scaleNonDB);

        //Set the values
        Set<String> set = new HashSet<>();
        set.addAll(getScalesSet(context));
        // add new set
        set.add(json);

        sharedPreferences.edit().putStringSet("scales", set).apply();
        Log.d("Scales", "Size: " + set.size());
    }

    public static void addStoppCriteria(StoppCriteria stoppCriteria, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);

        // save scale as JSON String
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).setPrettyPrinting();
        Gson gson = builder.create();

        String json = gson.toJson(stoppCriteria);

        //Set the values
        Set<String> set = new HashSet<>();
        set.addAll(getStoppCriteriaSet(context));
        // add new set
        set.add(json);

        sharedPreferences.edit().putStringSet("stoppCriteria", set).apply();
        Log.d("StoppCriteria", "Size: " + set.size());
    }

    public static void addStartCriteria(StartCriteria startCriteria, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);

        // save scale as JSON String
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).setPrettyPrinting();
        Gson gson = builder.create();

        String json = gson.toJson(startCriteria);

        //Set the values
        Set<String> set = new HashSet<>();
        set.addAll(getStartCriteriaSet(context));
        // add new set
        set.add(json);

        sharedPreferences.edit().putStringSet("startCriteria", set).apply();
        Log.d("StartCriteria", "Size: " + set.size());
    }

    /**
     * Get scales.
     *
     * @param context
     */
    private static Set<String> getScalesSet(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        return sharedPreferences.getStringSet("scales", new HashSet<String>());
    }

    /**
     * Get the patients as Set of JSON strings.
     *
     * @param context
     * @return
     */
    private static Set<String> getPatientsSet(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        return sharedPreferences.getStringSet("patients", new HashSet<String>());
    }

    private static Set<String> getStartCriteriaSet(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        return sharedPreferences.getStringSet("startCriteria", new HashSet<String>());
    }

    private static Set<String> getStoppCriteriaSet(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        return sharedPreferences.getStringSet("stoppCriteria", new HashSet<String>());
    }

    public static void resetScales(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        sharedPreferences.edit().putStringSet("scales", new HashSet<String>()).apply();
    }

    /**
     * Reset medical criteria.
     *
     * @param context
     */
    public static void resetCriteria(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        sharedPreferences.edit().putStringSet("startCriteria", new HashSet<String>()).apply();
        sharedPreferences.edit().putStringSet("stoppCriteria", new HashSet<String>()).apply();
    }

    /**
     * Get patients.
     *
     * @param context
     * @return
     */
    public static ArrayList<PatientFirebase> getPatients(Context context) {
        ArrayList<PatientFirebase> patients = new ArrayList<>();
        Set<String> patientsSet = getPatientsSet(context);

        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).setPrettyPrinting();
        Gson gson = builder.create();
        for (String currentPatientString : new ArrayList<>(patientsSet)) {
            PatientFirebase p = gson.fromJson(currentPatientString, PatientFirebase.class);
            patients.add(p);
        }

        // sort patients by name
        Collections.sort(patients, new Comparator<PatientFirebase>() {
            public int compare(PatientFirebase o1, PatientFirebase o2) {
                String first = StringHelper.removeAccents(o1.getName());
                String second = StringHelper.removeAccents(o2.getName());
                return first.compareTo(second);
            }
        });


        return patients;
    }


    public static ArrayList<GeriatricScaleNonDB> getScalesArrayList(Context context) {
        ArrayList<GeriatricScaleNonDB> scales = new ArrayList<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        Set<String> scalesSet = sharedPreferences.getStringSet("scales", new HashSet<String>());

        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).setPrettyPrinting();
        Gson gson = builder.create();
        for (String currentScaleString : new ArrayList<>(scalesSet)) {
            GeriatricScaleNonDB scaleNonDB = gson.fromJson(currentScaleString, GeriatricScaleNonDB.class);
            scales.add(scaleNonDB);
        }
        return scales;
    }

    public static ArrayList<StartCriteria> getStartCriteria(Context context) {
        ArrayList<StartCriteria> startCriteria = new ArrayList<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        Set<String> criteriaSet = sharedPreferences.getStringSet("startCriteria", new HashSet<String>());

        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).setPrettyPrinting();
        Gson gson = builder.create();
        for (String currentCriteriaString : new ArrayList<>(criteriaSet)) {
            StartCriteria criteria = gson.fromJson(currentCriteriaString, StartCriteria.class);
            startCriteria.add(criteria);
        }
        return startCriteria;
    }

    public static ArrayList<StoppCriteria> getStoppCriteria(Context context) {
        ArrayList<StoppCriteria> stoppCriteria = new ArrayList<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        Set<String> criteriaSet = sharedPreferences.getStringSet("stoppCriteria", new HashSet<String>());

        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).setPrettyPrinting();
        Gson gson = builder.create();
        for (String currentCriteriaString : new ArrayList<>(criteriaSet)) {
            StoppCriteria criteria = gson.fromJson(currentCriteriaString, StoppCriteria.class);
            stoppCriteria.add(criteria);
        }
        return stoppCriteria;
    }


    /**
     * Update a Patient.
     *
     * @param context
     * @param index
     * @param patient
     */
    public static void updatePatient(Context context, int index, PatientFirebase patient) {
        ArrayList<PatientFirebase> patients = getPatients(context);
        patients.set(index, patient);

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);

        // save scale as JSON String
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).setPrettyPrinting();
        Gson gson = builder.create();

        Set<String> set = new HashSet<>();

        for (PatientFirebase p : patients) {
            String patientJSON = gson.toJson(p);

            // add new patient
            set.add(patientJSON);
        }

        sharedPreferences.edit().putStringSet("patients", set).apply();
        Log.d("Patients", "Size: " + set.size());


    }


    public static void resetPatients(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        // empty set
        Set<String> set = new HashSet<>();
        sharedPreferences.edit().putStringSet("patients", set).apply();
    }

    /**
     * Save hash string user for cipher and decipher ops.
     *
     * @param hashString
     * @param context
     */
    public static void writeHashString(String hashString, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        sharedPreferences.edit().putString(context.getString(R.string.hashString), hashString).apply();
    }

    /**
     * Read Hash string.
     *
     * @param context
     * @return
     */
    public static String readHashString(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        return sharedPreferences.getString(context.getString(R.string.hashString), "");
    }

}
