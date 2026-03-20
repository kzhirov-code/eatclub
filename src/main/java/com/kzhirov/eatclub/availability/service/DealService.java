package com.kzhirov.eatclub.availability.service;

import java.time.LocalTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kzhirov.eatclub.availability.converter.DateConverter;
import com.kzhirov.eatclub.availability.converter.DealConverter;
import com.kzhirov.eatclub.availability.ds.RestaurantsDS;
import com.kzhirov.eatclub.availability.model.api.ActiveDealsResponse;
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
}
