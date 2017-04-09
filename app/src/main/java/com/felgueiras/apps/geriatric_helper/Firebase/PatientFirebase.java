package com.felgueiras.apps.geriatric_helper.Firebase;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class PatientFirebase implements Serializable {

    @Expose
    public String guid;

    @Expose
    private String name;

    @Expose
    private Date birthDate;

    @Expose
    private int gender;

    @Expose
    private int picture;

    @Expose
    private String address;

    @Expose
    private boolean favorite;

    @Expose
    private String notes;

    @Expose
    private String processNumber;

    @Expose
    private ArrayList<String> sessionsIDS = new ArrayList<>();

    @Expose
    private ArrayList<String> prescriptionsIDS = new ArrayList<>();


    private String key;


    public PatientFirebase(String patientsName, int gender, int image) {
        super();
        this.name = patientsName;
        this.gender = gender;
        this.picture = image;
    }

    public PatientFirebase() {
        super();
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }


    /*
    public static int[] getPatientsAges(ArrayList<Patient> patients) {
        int[] patientsAges = new int[patients.size()];
        for (int i = 0; i < patientsAges.length; i++) {
            patientsAges[i] = patients.get(i).age;
        }
        return patientsAges;
    }
    */


    public static int[] getPatientsPhotos(ArrayList<PatientFirebase> patients) {
        int[] patientsPhotos = new int[patients.size()];
        for (int i = 0; i < patientsPhotos.length; i++) {
            patientsPhotos[i] = patients.get(i).picture;
        }
        return patientsPhotos;
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    /**
     * Get a list of all the Patients
     *
     * @return
     */
//    public static ArrayList<PatientFirebase> getAllPatients() {
//        List<PatientFirebase> list = new Select().from(PatientFirebase.class).orderBy("patientName ASC").execute();
//        // order by first name - exclude accents
//
//        ArrayList<PatientFirebase> patients = new ArrayList<>();
//        patients.addAll(list);
//
//
//        Collections.sort(patients, new Comparator<PatientFirebase>() {
//            public int compare(PatientFirebase o1, PatientFirebase o2) {
//                String first = StringHelper.removeAccents(o1.getName());
//                String second = StringHelper.removeAccents(o2.getName());
//                return first.compareTo(second);
//            }
//        });
//        return patients;
//    }

    /**
     * Get a list of the favorite Patients
     *
     * @return
     */
//    public static ArrayList<PatientFirebase> getFavoritePatients() {
//        List<PatientFirebase> list = new Select().from(PatientFirebase.class).where("favorite = ?", true).orderBy("patientName ASC").execute();
//        ArrayList<PatientFirebase> patients = new ArrayList<>();
//        patients.addAll(list);
//        return patients;
//    }

    /**
     * Get records from PATIENT
     *
     * @return
     */
//    public ArrayList<Session> getSessionsFromPatient() {
//        List<Session> recordsList = new Select()
//                .from(Session.class)
//                .where("PATIENT = ?", this.getId())
//                .orderBy("date DESC")
//                .execute();
//        ArrayList<Session> sessionsIDS = new ArrayList<>();
//        sessionsIDS.addAll(recordsList);
//        return sessionsIDS;
//    }
    @Override
    public String toString() {
        return name + " - " + birthDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

//    public boolean isFirstSession() {
//        // get all sessionsIDS
//        List<Session> sessionsIDS = getMany(Session.class, "PATIENT");
//        return sessionsIDS.size() == 1;
//    }

    public int getAge() {
        Calendar a = getCalendar(birthDate);
        Calendar b = getCalendar(Calendar.getInstance().getTime());
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) ||
                (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.UK);
        cal.setTime(date);
        return cal;
    }

    public void setProcessNumber(String processNumber) {
        this.processNumber = processNumber;
    }

    public String getProcessNumber() {
        return processNumber;
    }

    public ArrayList<String> getSessionsIDS() {
        return sessionsIDS;
    }

    public void setSessionsIDS(ArrayList<String> sessionsIDS) {
        this.sessionsIDS = sessionsIDS;
    }


    public void addSession(String sessionID) {
        sessionsIDS.add(sessionID);
        // update patient
        FirebaseHelper.updatePatient(this);
    }

    public ArrayList<String> getPrescriptionsIDS() {
        return prescriptionsIDS;
    }

    public void setPrescriptionsIDS(ArrayList<String> prescriptionsIDS) {
        this.prescriptionsIDS = prescriptionsIDS;
    }


    public void addPrescription(String prescription) {
        prescriptionsIDS.add(prescription);
        // update patient
        FirebaseHelper.updatePatient(this);
    }
}
