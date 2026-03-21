package com.kzhirov.eatclub.availability.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.kzhirov.eatclub.availability.converter.DateConverter;
import com.kzhirov.eatclub.availability.converter.DealConverter;
import com.kzhirov.eatclub.availability.ds.RestaurantsFileBaseDS;
import com.kzhirov.eatclub.availability.model.api.ActiveDealsResponse;
import com.kzhirov.eatclub.availability.model.api.DealApiModel;
import com.kzhirov.eatclub.availability.model.api.PeakHoursResponse;

import tools.jackson.databind.ObjectMapper;

public class DealServiceTest {

    private final DealService dealService = new DealService(new RestaurantsFileBaseDS(new ObjectMapper()),
            new DateConverter(), new DealConverter());

    @Test
    void testIsTimeInRange_inRange() {
        assertTrue(dealService.isTimeInRange(LocalTime.of(9, 17), LocalTime.of(6, 0), LocalTime.of(14, 30)));
    }

    @Test
    void testIsTimeInRange_outOfRange() {
        assertTrue(!dealService.isTimeInRange(LocalTime.of(5, 55), LocalTime.of(6, 0), LocalTime.of(14, 30)));
    }

    @Test
    void testIsTimeInRange_inclStart() {
        assertTrue(dealService.isTimeInRange(LocalTime.of(6, 0), LocalTime.of(6, 0), LocalTime.of(14, 30)));
    }

    @Test
    void testIsTimeInRange_exclEnd() {
        assertTrue(!dealService.isTimeInRange(LocalTime.of(14, 30), LocalTime.of(6, 0), LocalTime.of(14, 30)));
    }

    @Test
    void testIsTimeInRange_overlapBeforeMidnight() {
        assertTrue(dealService.isTimeInRange(LocalTime.of(23, 55), LocalTime.of(21, 0), LocalTime.of(2, 30)));
    }

    @Test
    void testIsTimeInRange_overlapAfterMidnight() {
        assertTrue(dealService.isTimeInRange(LocalTime.of(1, 15), LocalTime.of(21, 0), LocalTime.of(2, 30)));
    }

    @Test
    void testIsTimeInRange_overlapOutOfRange() {
        assertTrue(!dealService.isTimeInRange(LocalTime.of(2, 45), LocalTime.of(21, 0), LocalTime.of(2, 30)));
    }

    @Test
    void testGetActiveDeals_someDeals() {
        ActiveDealsResponse response = dealService.getActiveDeals(LocalTime.of(13, 0));
        assertEquals(4, response.deals().size());
        Set<String> activeDeals = Set.of(
                "D80263E8-1111-2C70-FF6B-D854ADB8DB00",
                "B5913CD0-0550-40C7-AFC3-7D46D26B01BF",
                "B5713CD0-1361-40C7-AFC3-7D46D26B00BF",
                "D80263E8-0000-2C70-FF6B-D854ADB8DB00");
        Set<String> result = response.deals().stream().map(DealApiModel::dealObjectId).collect(Collectors.toSet());
        assertEquals(result, activeDeals);
    }

    @Test
    void testPeakHours() {
        PeakHoursResponse peakHoursResponse = dealService.getPeakHours();
        assertEquals("5:00pm", peakHoursResponse.peakTimeStart());
        assertEquals("9:00pm", peakHoursResponse.peakTimeEnd());
    }

    @Test
    void testPeakHours_midnight() {
        DealService service = new DealService(new RestaurantsFileBaseDS(new ObjectMapper(), "testData.json"), new DateConverter(), new DealConverter());
        PeakHoursResponse result = service.getPeakHours();
        // any of the intervals 9pm-12am or 12am-1am 
        assertTrue(result.peakTimeStart().equals("9:00pm") || result.peakTimeStart().equals("12:00am"));
        assertTrue(result.peakTimeEnd().equals("1:00am") || result.peakTimeEnd().equals("12:00am"));

    }
}