package com.felgueiras.apps.geriatric_helper.Firebase;


import com.felgueiras.apps.geriatric_helper.HelpersHandlers.DatesHandler;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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


    /**
     * Get all the scales from this Session.
     *
     * @return
     */
//    public List<GeriatricScale> getScalesFromSession() {
//        return getMany(GeriatricScale.class, "session");
//    }

//
//    public GeriatricScale getScaleByName(String scaleName) {
//
//        List<GeriatricScale> scalesFromSession = getMany(GeriatricScale.class, "session");
//        for (int i = 0; i < scalesFromSession.size(); i++) {
//            if (scalesFromSession.get(i).getScaleName().equals(scaleName)) {
//                return scalesFromSession.get(i);
//            }
//        }
//        return null;
//    }

//    public ArrayList<GeriatricScale> getScalesFromArea(String area) {
//
//        List<GeriatricScale> scalesFromSession = getMany(GeriatricScale.class, "session");
//        ArrayList<GeriatricScale> scalesForThisArea = new ArrayList<>();
//        for (int i = 0; i < scalesFromSession.size(); i++) {
//            GeriatricScale currentScale = scalesFromSession.get(i);
//            if (currentScale.getArea().equals(area)) {
//                scalesForThisArea.add(currentScale);
//            }
//        }
//        return scalesForThisArea;
//    }


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

//    public PatientFirebase getPatient() {
//        return patient;
//    }

    /**
     * Set the Patient for a Session
     *
     * @param patient
     */
//    public void setPatient(Patient patient) {
//        this.patient = patient;
//    }

    //retrieve all items
//    public static ArrayList<SessionFirebase> getAllSessions() {
//        List<SessionFirebase> list = new Select().from(SessionFirebase.class).orderBy("guid DESC").execute();
//        ArrayList<SessionFirebase> sessions = new ArrayList<>();
//        sessions.addAll(list);
//        return sessions;
//    }

    /**
     * Get NewEvaluationPrivate from a certain Date
     *
     * @param firstDay
     * @return
     */
//    public static List<SessionFirebase> getSessionsFromDate(Date firstDay) {
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(firstDay.getTime());
//
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
//        cal.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
//        cal.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
//        cal.set(Calendar.HOUR_OF_DAY, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.SECOND, 0);
//        // first day
//        firstDay = cal.getTime();
//        // second day
//        cal.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
//        Date secondDay = cal.getTime();
//
//        //system.out.println("Getting sessions from " + firstDay + "-" + secondDay);
//        return new Select()
//                .from(SessionFirebase.class)
//                .where("date > ? and date < ?", firstDay.getTime(), secondDay.getTime())
//                .orderBy("guid ASC")
//                .execute();
//    }


    /**
     * Get a Session by its sessionIDString
     *
//     * @param sessionID ID of the Session to be retrieved
     * @return
     */
//    public static SessionFirebase getSessionByID(String sessionID) {
//        return new Select()
//                .from(SessionFirebase.class)
//                .where("guid = ?", sessionID)
//                .executeSingle();
//    }


    // TODO get in order
//    public static ArrayList<Date> getDifferentSessionDates() {
//        List<SessionFirebase> dates = new Select()
//                .distinct()
//                .from(SessionFirebase.class)
//                .groupBy("date")
//                .orderBy("date ASC")
//                .execute();
//        HashSet<Date> days = new HashSet<>();
//        for (SessionFirebase session : dates) {
//            Date dateWithoutHour = session.getDateWithoutHour();
//            days.add(dateWithoutHour);
//        }
//        ArrayList<Date> differentDates = new ArrayList<>();
//        differentDates.addAll(days);
//        // order by date (descending)
//        Collections.sort(differentDates, new Comparator<Date>() {
//            @Override
//            public int compare(Date first, Date second) {
//                if (first.after(second)) {
//                    return -1;
//                } else if (first.before(second)) {
//                    return 1;
//                } else
//                    return 0;
//            }
//        });
//        return differentDates;
//    }


//    public void eraseScalesNotCompleted() {
//        List<GeriatricScale> finalTests = getScalesFromSession();
//        for (GeriatricScale scaleID : finalTests) {
//            if (!scaleID.isCompleted()) {
//                scaleID.setSessionID(null);
//                scaleID.delete();
//            }
//        }
//
//    }

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
        FirebaseHelper.updateSession(this);
    }
}
