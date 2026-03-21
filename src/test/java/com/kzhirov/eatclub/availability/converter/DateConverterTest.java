package com.kzhirov.eatclub.availability.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.Test;

public class DateConverterTest {
    DateConverter dateConverter = new DateConverter();

    @Test
    void testConvert_null() {
        assertThrows(IllegalArgumentException.class, () -> dateConverter.convert(null));
    }

    @Test
    void testConvert_wrongFormat() {
        assertThrows(DateTimeParseException.class, () -> dateConverter.convert("25:00am"));
    }

    @Test
    void testConvert_convert() {
        // midnight
        assertEquals(dateConverter.convert("12:00am"), LocalTime.of(0, 0));
        // funky case Am
        assertEquals(dateConverter.convert("9:00Am"), LocalTime.of(9, 0));
        // minutes
        assertEquals(dateConverter.convert("11:59pm"), LocalTime.of(23, 59));
    }

    @Test
    void testConvertFromLocalTime_null() {
        assertThrows(IllegalArgumentException.class, () -> dateConverter.convertFromLocalTime(null));
    }

    @Test
    void testConvertFromLocalTime_convert() {
        assertEquals(dateConverter.convertFromLocalTime(LocalTime.of(0, 0)), "12:00am");
        assertEquals(dateConverter.convertFromLocalTime(LocalTime.of(9, 0)), "9:00am");
        assertEquals(dateConverter.convertFromLocalTime(LocalTime.of(23, 59)), "11:59pm");
    }
}
