package com.felgueiras.apps.geriatric_helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.felgueiras.apps.geriatric_helper.Firebase.CipherDecipherFiles;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseStorageHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PrescriptionFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.SharedPreferencesHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by felgueiras on 21/04/2017.
 */

public class PatientsManagement {

//    static ArrayList<PatientFirebase> patients = new ArrayList<>();

    boolean initialFetch = false;

    private static PatientsManagement singleton = new PatientsManagement();

    public static PatientsManagement getInstance() {
        return singleton;
    }

    private PatientsManagement() {
    }

    /**
     * Save a Patient.
     *
     * @param patient
     * @param context
     */
    public void createPatient(PatientFirebase patient, Context context) {

        // persist to shared preferences
        SharedPreferencesHelper.addPatient(patient, context);
    }


    /**
     * Get patient associated with a prescription.
     *
     * @param prescription
     * @param context
     * @return
     */
    public PatientFirebase getPatientFromPrescription(PrescriptionFirebase prescription, Context context) {
        ArrayList<PatientFirebase> patients = getPatients(context);
        for (PatientFirebase patient : patients) {
            if (patient.getGuid().equals(prescription.getPatientID())) {
                return patient;
            }
        }
        return null;

    }

    /**
     * Get the list of patients.
     *
     * @param context
     * @return
     */
    public ArrayList<PatientFirebase> getPatients(Context context) {

        return SharedPreferencesHelper.getPatients(context);
    }

    public static void loadInitialPatients(Context context) {
        Log.d("Patients", "Loading initial list of patients");
        // fetch from Firebase and save to shared preferences
         FirebaseStorageHelper.getPatientsBackEnd(context);
    }


    public ArrayList<PatientFirebase> getFavoritePatients(Context context) {
        ArrayList<PatientFirebase> favoritePatients = new ArrayList<>();
        for (PatientFirebase patient : SharedPreferencesHelper.getPatients(context)) {
            if (patient.isFavorite())
                favoritePatients.add(patient);
        }
        return favoritePatients;
    }

    public void updatePatient(PatientFirebase patient, Context context) {
        ArrayList<PatientFirebase> patients = getPatients(context);
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getGuid().equals(patient.getGuid())) {
                patients.set(i, patient);
                // persist in Shared Preferences
                SharedPreferencesHelper.updatePatient(context, i, patient);
                return;
            }
        }
    }

    public void deletePatient(PatientFirebase patient, Context context) {
        // delete sessions from this patient
        ArrayList<SessionFirebase> sessions = FirebaseDatabaseHelper.getSessionsFromPatient(patient);
        for (SessionFirebase session : sessions) {
            FirebaseDatabaseHelper.deleteSession(session, context);
        }

        // delete prescriptions from this patient
        ArrayList<PrescriptionFirebase> prescriptions = FirebaseDatabaseHelper.getPrescriptionsFromPatient(patient);
        for (PrescriptionFirebase prescription : prescriptions) {
            FirebaseDatabaseHelper.deletePrescription(prescription, context);
        }

//        FirebaseHelper.firebaseTablePatients.child(patient.getKey()).removeValue();
    }

    /**
     * Get a patient by its ID.
     *
     * @return
     */
    public PatientFirebase getPatientFromSession(SessionFirebase session, Context context) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            return null;
        }
        if (session.getPatientID() == null) {
            return null;
        }
        ArrayList<PatientFirebase> patients = getPatients(context);
        for (PatientFirebase patient : patients) {
            if (patient.getGuid().equals(session.getPatientID()))
                return patient;
        }
        return null;
    }
}
