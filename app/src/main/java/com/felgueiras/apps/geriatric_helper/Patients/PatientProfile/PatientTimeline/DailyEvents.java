package com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientTimeline;

import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PrescriptionFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.SessionFirebase;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by felgueiras on 01/05/2017.
 */

class DailyEvents {

    private final Date date;
    ArrayList<SessionFirebase> sessions = new ArrayList<>();
    ArrayList<PrescriptionFirebase> prescriptions = new ArrayList<>();

    public DailyEvents(Date date, ArrayList<PrescriptionFirebase> prescriptionsForDate, ArrayList<SessionFirebase> sessionsForDate) {
        this.date = date;
        this.prescriptions = prescriptionsForDate;
        this.sessions = sessionsForDate;
    }

    public Date getDate() {
        return date;
    }

    public ArrayList<SessionFirebase> getSessions() {
        return sessions;
    }

    public ArrayList<PrescriptionFirebase> getPrescriptions() {
        return prescriptions;
    }
}
