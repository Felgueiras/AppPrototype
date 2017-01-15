package com.example.rafael.appprototype;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by felgueiras on 15/01/2017.
 */

public class DatesHandler {

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

    public static String dateToStringDayWithoutHour(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String datetime;
        datetime = format.format(date);
        return datetime;
    }

    public static Date createCustomDate(int year, int month, int day, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }
}
