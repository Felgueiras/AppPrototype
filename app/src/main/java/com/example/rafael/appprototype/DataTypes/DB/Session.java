package com.example.rafael.appprototype.DataTypes.DB;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.example.rafael.appprototype.HelpersHandlers.DatesHandler;
import com.google.gson.annotations.Expose;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Created by rafael on 15-09-2016.
 */
@Table(name = "Sessions")
public class Session extends Model implements Serializable {

    @Expose
    @Column(name = "date")
    Date date;

    @Expose
    @Column(name = "guid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    String guid;

    @Expose
    @Column(name = "patient", onDelete = Column.ForeignKeyAction.CASCADE)
    Patient patient;

    @Expose
    @Column(name = "notes")
    String  notes;


    /**
     * Get all the scales from this Session.
     *
     * @return
     */
    public List<GeriatricScale> getScalesFromSession() {
        return getMany(GeriatricScale.class, "session");
    }


    public GeriatricScale getScaleByName(String scaleName) {

        List<GeriatricScale> scalesFromSession = getMany(GeriatricScale.class, "session");
        for (int i = 0; i < scalesFromSession.size(); i++) {
            if (scalesFromSession.get(i).getScaleName().equals(scaleName)) {
                return scalesFromSession.get(i);
            }
        }
        return null;
    }

    public ArrayList<GeriatricScale> getScalesFromArea(String area) {

        List<GeriatricScale> scalesFromSession = getMany(GeriatricScale.class, "session");
        ArrayList<GeriatricScale> scalesForThisArea = new ArrayList<>();
        for (int i = 0; i < scalesFromSession.size(); i++) {
            GeriatricScale currentScale = scalesFromSession.get(i);
            if (currentScale.getArea().equals(area)) {
                scalesForThisArea.add(currentScale);
            }
        }
        return scalesForThisArea;
    }


    public Session() {
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
    public Date getDate() {
        return date;
    }

    /**
     * Get date (not including hour and minutes).
     *
     * @return
     */
    public Date getDateWithoutHour() {
        // create a copy of the date with hour and minute set to 0


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        return DatesHandler.createCustomDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                0, 0);

    }

    public void setDate(Date date) {
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

    public Patient getPatient() {
        return patient;
    }

    /**
     * Set the Patient for a Session
     *
     * @param patient
     */
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    //retrieve all items
    public static ArrayList<Session> getAllSessions() {
        List<Session> list = new Select().from(Session.class).orderBy("guid DESC").execute();
        ArrayList<Session> sessions = new ArrayList<>();
        sessions.addAll(list);
        return sessions;
    }

    /**
     * Get NewEvaluationPrivate from a certain Date
     *
     * @param firstDay
     * @return
     */
    public static List<Session> getSessionsFromDate(Date firstDay) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(firstDay.getTime());

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        // first day
        firstDay = cal.getTime();
        // second day
        cal.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        Date secondDay = cal.getTime();

        //system.out.println("Getting sessions from " + firstDay + "-" + secondDay);
        return new Select()
                .from(Session.class)
                .where("date > ? and date < ?", firstDay.getTime(), secondDay.getTime())
                .orderBy("guid ASC")
                .execute();
    }


    /**
     * Get a Session by its sessionIDString
     *
     * @param sessionID ID of the Session to be retrieved
     * @return
     */
    public static Session getSessionByID(String sessionID) {
        return new Select()
                .from(Session.class)
                .where("guid = ?", sessionID)
                .executeSingle();
    }


    public static ArrayList<Date> getDifferentSessionDates() {
        List<Session> dates = new Select()
                .distinct()
                .from(Session.class)
                .groupBy("date")
                .orderBy("date ASC")
                .execute();
        HashSet<Date> days = new HashSet<>();
        for (Session session : dates) {
            Date dateWithoutHour = session.getDateWithoutHour();
            days.add(dateWithoutHour);
        }
        ArrayList<Date> differentDates = new ArrayList<>();
        differentDates.addAll(days);
        return differentDates;
    }


    public void eraseScalesNotCompleted() {
        List<GeriatricScale> finalTests = getScalesFromSession();
        for (GeriatricScale test : finalTests) {
            if (!test.isCompleted()) {
                test.setSession(null);
                test.delete();
            }
        }

    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
