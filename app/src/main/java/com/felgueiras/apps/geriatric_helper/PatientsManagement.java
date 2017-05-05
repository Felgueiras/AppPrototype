package com.felgueiras.apps.geriatric_helper;

import android.content.Context;
import android.util.Log;

import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseStorageHelper;
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
    public void addPatient(PatientFirebase patient, Context context) {

        // persist locally
        SharedPreferencesHelper.addPatient(patient, context);

        // persist in backend
        FirebaseStorageHelper.getInstance().addPatientBackEnd(context, patient);
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

    /**
     * Load patients when entering PrivateArea.
     *
     * @param context
     */
    public static void loadInitialPatients(Context context) {
        // fetch from Firebase and save to shared preferences
        FirebaseStorageHelper.getInstance().getPatients(context);
    }


    public ArrayList<PatientFirebase> getFavoritePatients(Context context) {
        ArrayList<PatientFirebase> favoritePatients = new ArrayList<>();
        for (PatientFirebase patient : SharedPreferencesHelper.getPatients(context)) {
            if (patient.isFavorite())
                favoritePatients.add(patient);
        }
        return favoritePatients;
    }

    /**
     * Update a patient.
     *
     * @param patient
     * @param context
     */
    public void updatePatient(PatientFirebase patient, Context context) {
        ArrayList<PatientFirebase> patients = getPatients(context);
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getGuid().equals(patient.getGuid())) {
                patients.set(i, patient);
                // persist in Shared Preferences
                Log.d("Patients", "Updated patient");
                SharedPreferencesHelper.updatePatient(context, i, patient);

                // update in Firebase
                FirebaseStorageHelper.getInstance().updatePatient(context, patient);

                return;
            }
        }
    }

    /**
     * Delete a patient.
     *
     * @param patient
     * @param context
     */
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

        // remove from storage
        FirebaseStorageHelper.getInstance().removePatientFile(patient.getGuid());

        // remove from database
        FirebaseHelper.firebaseTablePatients.child(patient.getGuid()).removeValue();

        // remove from shared preferences
        SharedPreferencesHelper.removePatient(patient, context);


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
