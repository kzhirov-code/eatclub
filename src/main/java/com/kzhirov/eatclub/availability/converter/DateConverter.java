package com.kzhirov.eatclub.availability.converter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.stereotype.Component;

@Component
public class DateConverter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("h:mma", Locale.ENGLISH);

    public LocalTime convert(String timeOfDay) {
        if (timeOfDay == null || timeOfDay.isEmpty()) {
            throw new IllegalArgumentException("Time of day cannot be null or empty");
        }
        return LocalTime.parse(timeOfDay.toUpperCase(), FORMATTER);
    }

    public String convertFromLocalTime(LocalTime time) {
        if (time == null) {
            throw new IllegalArgumentException("Time cannot be null");
        }
        return time.format(FORMATTER).toLowerCase();
    }
}
