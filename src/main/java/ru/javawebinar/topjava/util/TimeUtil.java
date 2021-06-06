package ru.javawebinar.topjava.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    private TimeUtil() {}

    private static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }

    public static DateTimeFormatter getFormatter() {
        return formatter;
    }
}
