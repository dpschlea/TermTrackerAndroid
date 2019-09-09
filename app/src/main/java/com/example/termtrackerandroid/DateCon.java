package com.example.termtrackerandroid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateCon {

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd h:mm a z", Locale.getDefault());

    public static long getDateTimestamp(String dateInput) {
        try {
            Date date = DateCon.dateFormat.parse(dateInput + TimeZone.getDefault().getDisplayName());
            return date.getTime();
        }
        catch (ParseException e) {
            return 0;
        }
    }

    public static long todayLong() {
        String currentDate = DateCon.dateFormat.format(new Date());
        return getDateTimestamp(currentDate);
    }

    public static long todayLongTime() {
        return System.currentTimeMillis();
    }
}