package com.example.rafael.appprototype.DataTypes.DB;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.example.rafael.appprototype.DataTypes.Patient;


import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by rafael on 15-09-2016.
 */
@Table(name = "Sessions")
public class Session extends Model implements Serializable {

    @Column(name = "date")
    String date;
    @Column(name = "guid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    String guid;
    @Column(name = "patient", onDelete = Column.ForeignKeyAction.CASCADE)
    Patient patient;


    public List<GeriatricTest> getTestsFromSession() {
        return getMany(GeriatricTest.class, "session");
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


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
    public static List<Session> getAllSessions() {
        return new Select().from(Session.class).orderBy("guid DESC").execute();
    }

    /**
     * Get NewEvaluation from a certain Date
     *
     * @param date
     * @return
     */
    public static List<Session> getSessionsFromDate(String date) {
        return new Select()
                .from(Session.class)
                .where("date = ?", date)
                .orderBy("guid ASC")
                .execute();
    }


    /**
     * Get a Session by its sessionIDString
     * @param sessionID ID of the Session to be retrieved
     * @return
     */
    public static Session getSessionByID(String sessionID) {
        return new Select()
                .from(Session.class)
                .where("guid = ?", sessionID)
                .executeSingle();
    }

    /**
     * Get all the different record dates
     *
     * @return
     */
    public static List<Session> getSessionDates() {
        return new Select()
                .distinct()
                .from(Session.class)
                .groupBy("date")
                .orderBy("date DESC")
                .execute();
    }

    public static Date createCustomDate(int year, int month, int day, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);

        /*
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        */
        return cal.getTime();
    }

    /**
     * Convert a Date in String format to Date
     *
     * @param dateString Date in String format
     * @return Date object for that String
     */
    public static Date stringToDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(dateString);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return date;
    }

    /**
     * Convert a Date to a String
     *
     * @param date Date object
     * @return String representation of that Date
     */
    public static String dateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String datetime;
        datetime = format.format(date);
        return datetime;
    }

    public Date getDateAsString() {
        return stringToDate(date);
    }
}
