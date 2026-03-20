package com.kzhirov.eatclub.availability.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kzhirov.eatclub.availability.converter.DateConverter;
import com.kzhirov.eatclub.availability.converter.DealConverter;
import com.kzhirov.eatclub.availability.ds.RestaurantsDS;
import com.kzhirov.eatclub.availability.model.DealEvent;
import com.kzhirov.eatclub.availability.model.api.ActiveDealsResponse;
import com.kzhirov.eatclub.availability.model.api.PeakHoursResponse;
import com.kzhirov.eatclub.availability.model.dto.DealDto;
import com.kzhirov.eatclub.availability.model.dto.RestaurantDto;
import com.kzhirov.eatclub.availability.model.dto.RestaurantsInfoDto;

@Service
public class DealService {

    private final RestaurantsDS restaurantsDS;
    private final DateConverter dateConverter;
    private final DealConverter dealConverter;

    @Autowired
    public DealService(RestaurantsDS restaurantsDS, DateConverter dateConverter, DealConverter dealConverter) {
        this.restaurantsDS = restaurantsDS;
        this.dateConverter = dateConverter;
        this.dealConverter = dealConverter;
    }

    public ActiveDealsResponse getActiveDeals(LocalTime timeOfDay) {
        RestaurantsInfoDto restaurantsInfo = restaurantsDS.getRestaurants();
        ActiveDealsResponse result = new ActiveDealsResponse(new ArrayList<>());
        for (RestaurantDto restaurantDto: restaurantsInfo.restaurants()) {
            LocalTime restaurantOpen = dateConverter.convert(restaurantDto.open());
            LocalTime restaurantClose = dateConverter.convert(restaurantDto.close());
            if (!isTimeInRange(timeOfDay, restaurantOpen, restaurantClose)) {
                continue;
            }
            for (DealDto dealDto: restaurantDto.deals()) {
                // TODO check that qtyLeft > 0
                // if (dealDto.qtyLeft() == null || Integer.parseInt(dealDto.qtyLeft()) = 0) {
                //   continue;
                // }

                LocalTime dealStart = dealDto.start() != null
                    ? dateConverter.convert(dealDto.start())
                    : restaurantOpen;
                LocalTime dealEnd = dealDto.end() != null
                    ? dateConverter.convert(dealDto.end())
                    : restaurantClose;
                if (isTimeInRange(timeOfDay, dealStart, dealEnd)) {
                    result.deals().add(dealConverter.convert(restaurantDto, dealDto));
                }
            }
        }
        return result;
    }

    boolean isTimeInRange(LocalTime time, LocalTime start, LocalTime end)  {
        // imaging restaurant opens at 9pm and closes at 2am
        if (start.isAfter(end)) {
            return !time.isBefore(start) || time.isBefore(end);
        }
        else {
            return !time.isBefore(start) && time.isBefore(end);
        }
    }

    public PeakHoursResponse getPeakHours() {
        List<DealEvent> events = getDealEvents();
        int count = 0;
        LocalTime peakStart = null;
        LocalTime peakEnd = null;
        LocalTime currentStart = null;
        int maxCount = 0;
        for (DealEvent event: events) {
            if (event.increase()) {
                count++;
                currentStart = event.time();
            } else {
                if (count > maxCount) {
                    maxCount = count;
                    peakStart = currentStart;
                    peakEnd = event.time();
                }
                count--;
            }
        }

        return new PeakHoursResponse(
            dateConverter.convertFromLocalTime(peakStart), 
            dateConverter.convertFromLocalTime(peakEnd)
        );
    }

    /**
     * Converts deals to Localtime events starting/ending and sorts by start time
     * @return
     */
    List<DealEvent> getDealEvents() {
        RestaurantsInfoDto restaurantsInfo = restaurantsDS.getRestaurants();
        List<DealEvent> events = new ArrayList<>();
        for (RestaurantDto restaurantDto: restaurantsInfo.restaurants()) {
            LocalTime restaurantOpen = dateConverter.convert(restaurantDto.open());
            LocalTime restaurantClose = dateConverter.convert(restaurantDto.close());
            for (DealDto dealDto: restaurantDto.deals()) {
                // TODO check that qtyLeft > 0
                // if (dealDto.qtyLeft() == null || Integer.parseInt(dealDto.qtyLeft()) = 0) {
                //   continue;
                // }

                LocalTime dealStart = dealDto.start() != null
                    ? dateConverter.convert(dealDto.start())
                    : restaurantOpen;
                LocalTime dealEnd = dealDto.end() != null
                    ? dateConverter.convert(dealDto.end())
                    : restaurantClose;
                if (dealStart.isAfter(dealEnd)) {
                    // deal starts at 9pm and ends at 2am
                    events.add(new DealEvent(dealStart, true)); 
                    events.add(new DealEvent(LocalTime.MAX, false));
                    events.add(new DealEvent(LocalTime.MIDNIGHT, true));
                    events.add(new DealEvent(dealEnd, false));
                } else {
                    events.add(new DealEvent(dealStart, true)); 
                    events.add(new DealEvent(dealEnd, false));
                }
            }
        }

        Collections.sort(events, Comparator.comparing(DealEvent::time));
        return events;
    }

}
