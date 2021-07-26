package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

public class StringToLocalTimeFormatter implements Formatter<LocalTime> {
    @Override
    public LocalTime parse(String text, Locale locale) {
        return parseLocalTime(text);
    }

    @Override
    public String print(LocalTime object, Locale locale) {
        return object.format(DateTimeFormatter.ISO_LOCAL_TIME);
    }
}
