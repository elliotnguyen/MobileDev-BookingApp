package com.example.ticketbooking.Calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DateUtils {
    public static String getCurrentDate() {
        /*DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM, yyyy");
        Date date = new Date();
        return dateFormat.format(date);*/
        return "Fri, 24 Nov, 2023";
    }
    public static String getNextDay(String currentDate) {
        return getOffsetDate(currentDate, 1);
    }
    public static String getPreviousDay(String currentDate) {
        return getOffsetDate(currentDate, -1);
    }
    public static String getOffsetDate(String currentDate, int days) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM, yyyy");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(currentDate));
            calendar.add(Calendar.DAY_OF_YEAR, days);
            Date newDate = calendar.getTime();
            return dateFormat.format(newDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
