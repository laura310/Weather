package com.sjsu.cmpe277.weather.DataModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by laurazhou on 11/3/17.
 */

public class TimeConverter {

    public static String getTimeInFormat(long epochTime, String timezoneId, String formatStr) {
        Date date = new Date(epochTime * 1000L);
        DateFormat format = new SimpleDateFormat(formatStr);
        format.setTimeZone(TimeZone.getTimeZone(timezoneId));

        return format.format(date);
    }
}
