package com.kzhirov.eatclub.availability.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

public class DateConverterTest {
    DateConverter dateConverter = new DateConverter();

    @Test
    void testConvert_null() {
        assertThrows(IllegalArgumentException.class, () -> dateConverter.convert(null));
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
}
