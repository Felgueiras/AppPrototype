package com.felgueiras.apps.geriatric_helper;

import android.content.Context;
import android.support.annotation.Nullable;

import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PrescriptionFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.SharedPreferencesHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * Created by felgueiras on 21/04/2017.
 */

public class PatientsManagement {

//    static ArrayList<PatientFirebase> patients = new ArrayList<>();

    /**
     * Save a Patient.
     *
     * @param patient
     * @param context
     */
    public static void createPatient(PatientFirebase patient, Context context) {

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
    public static PatientFirebase getPatientFromPrescription(PrescriptionFirebase prescription, Context context) {
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
    public static ArrayList<PatientFirebase> getPatients(Context context) {
        return SharedPreferencesHelper.getPatients(context);
    }

    public static ArrayList<PatientFirebase> getFavoritePatients(Context context) {
        ArrayList<PatientFirebase> favoritePatients = new ArrayList<>();
        for (PatientFirebase patient : SharedPreferencesHelper.getPatients(context)) {
            if (patient.isFavorite())
                favoritePatients.add(patient);
        }
        return favoritePatients;
    }

    public static void updatePatient(PatientFirebase patient, Context context) {
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

    public static void deletePatient(PatientFirebase patient, Context context) {
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
    @Nullable
    public static PatientFirebase getPatientFromSession(SessionFirebase session, Context context) {
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
