package com.example.rafael.appprototype.DataTypes;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.example.rafael.appprototype.DataTypes.DB.Session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafael on 15-09-2016.
 */
@Table(name = "Patients")
public class Patient extends Model implements Serializable {

    @Column(name = "guid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String guid;
    @Column(name = "name")
    private String name;
    @Column(name = "birthDate")
    private String birthDate;
    @Column(name = "gender")
    private int gender;
    @Column(name = "type")
    private int age;
    @Column(name = "picture")
    private int picture;
    @Column(name = "address")
    private String address;

    public Patient(String patientsName, int gender, int image) {
        super();
        this.name = patientsName;
        this.gender = gender;
        this.picture = image;
    }

    public Patient() {
        super();

    }


    public int getAge() {
        return age;
    }

    public static String[] getPatientsNames(ArrayList<Patient> patients) {
        String[] patientsNames = new String[patients.size()];
        for (int i = 0; i < patientsNames.length; i++) {
            patientsNames[i] = patients.get(i).name;
        }
        return patientsNames;
    }

    public static int[] getPatientsAges(ArrayList<Patient> patients) {
        int[] patientsAges = new int[patients.size()];
        for (int i = 0; i < patientsAges.length; i++) {
            patientsAges[i] = patients.get(i).age;
        }
        return patientsAges;
    }


    public static int[] getPatientsPhotos(ArrayList<Patient> patients) {
        int[] patientsPhotos = new int[patients.size()];
        for (int i = 0; i < patientsPhotos.length; i++) {
            patientsPhotos[i] = patients.get(i).picture;
        }
        return patientsPhotos;
    }


    public void setAge(int age) {
        this.age = age;
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
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

    /**
     * Get a list of all the Patients
     *
     * @return
     */
    public static ArrayList<Patient> getAllPatients() {
        List<Patient> list = new Select().from(Patient.class).orderBy("name ASC").execute();
        ArrayList<Patient> patients = new ArrayList<>();
        patients.addAll(list);
        return patients;
    }

    /**
     * Get records from patient
     *
     * @return
     */
    public ArrayList<Session> getRecordsFromPatient() {
        List<Session> recordsList = getMany(Session.class, "patient");
        ArrayList<Session> sessions = new ArrayList<>();
        sessions.addAll(recordsList);
        return sessions;
    }

    @Override
    public String toString() {
        return name + " - " + age;
    }
}
