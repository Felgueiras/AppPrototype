package com.felgueiras.apps.geriatric_helper.HelpersHandlers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
        Date date = null;
        try {
            date = format.parse(dateString);
            //system.out.println(date);
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
    public static String dateToStringDayMonthYear(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        String datetime;
        datetime = format.format(date);
        return datetime;
    }

    public static String dateToStringWithoutHour(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String datetime;
        datetime = format.format(date);

        // get current date - if they match day is 'Hoje'
        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();
        String datetimeCurrent;
        datetimeCurrent = format.format(currentDate);

        if (datetime.equals(datetimeCurrent))
            return "Hoje";
        else
            return datetime;
    }

    public static String dateToStringWithHour(Date date) {
        /**
         * Day.
         */
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd/MM/yyyy");
        String day = dayFormat.format(date);


        // get current date - if they match day is 'Hoje'
        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();
        String datetimeCurrent;
        datetimeCurrent = dayFormat.format(currentDate);

        String ret = "";

        if (day.equals(datetimeCurrent))
            ret += "Hoje";
        else
            ret += day;

        /**
         * Hour.
         */
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm",Locale.UK);
        String hour = hourFormat.format(date);

        ret+= " - " + hour;
        return ret;
    }

    public static String hour(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.UK);
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

    /**
     * Get date (not including hour and minutes).
     *
     * @return
     * @param date
     */
    public static Date getDateWithoutHour(long date) {
        // create a copy of the date with hour and minute set to 0

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return DatesHandler.createCustomDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                0, 0);
    }
}
