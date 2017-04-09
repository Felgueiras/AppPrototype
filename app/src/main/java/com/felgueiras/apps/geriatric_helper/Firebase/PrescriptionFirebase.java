package com.felgueiras.apps.geriatric_helper.Firebase;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class PrescriptionFirebase implements Serializable {

    @Expose
    public String guid;

    @Expose
    private String name;

    @Expose
    private String notes;

    @Expose
    private Date date;

    private String key;


    @Expose
    private String patientID;


    public PrescriptionFirebase(String name, String notes, Date date) {
        super();
        this.name = name;
        this.notes = notes;
        this.date = date;
    }

    public PrescriptionFirebase() {
        super();
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }


    public String getName() {
        return name;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.UK);
        cal.setTime(date);
        return cal;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }
}
