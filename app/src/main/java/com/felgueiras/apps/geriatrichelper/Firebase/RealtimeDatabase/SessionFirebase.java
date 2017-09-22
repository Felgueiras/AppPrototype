package com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase;


import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rafael on 15-09-2016.
 */
public class SessionFirebase implements Serializable {

    @Expose
    long date;

    @Expose
    String guid;

    @Expose
    String patientID;

    @Expose
    String  notes;

    @Expose
    private ArrayList<String> scalesIDS = new ArrayList<>();

    private String key;



    public SessionFirebase() {
        super();
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }


    /**
     * Get date (including hour and minutes).
     *
     * @return
     */
    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }


    @Override
    public String toString() {
        String ret = "";
        ret += guid + "\n";
        ret += date + "\n";
        /*
        for (int i = 0; i < tests.size(); i++) {
            ret += "\t" + tests.get(i).toString() + "\n";
        }
        */
        return ret;
    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<String> getScalesIDS() {
        return scalesIDS;
    }

    public void setScalesIDS(ArrayList<String> scalesIDS) {
        this.scalesIDS = scalesIDS;
    }

    public void addScaleID(String scaleID)
    {
        scalesIDS.add(scaleID);
        FirebaseDatabaseHelper.updateSession(this);
    }
}
