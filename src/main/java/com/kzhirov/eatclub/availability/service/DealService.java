package com.kzhirov.eatclub.availability.service;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kzhirov.eatclub.availability.ds.RestaurantsDS;
import com.kzhirov.eatclub.availability.model.api.ActiveDealsResponse;

@Service
public class DealService {

    private final RestaurantsDS restaurantsDS;


    @Autowired
    public DealService(RestaurantsDS restaurantsDS) {
        this.restaurantsDS = restaurantsDS;
    }


    public ActiveDealsResponse getActiveDeals(LocalTime timeOfDay) {
        return null;
    }
}
