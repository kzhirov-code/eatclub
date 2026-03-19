package com.kzhirov.eatclub.availability.controller;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kzhirov.eatclub.availability.converter.DateConverter;
import com.kzhirov.eatclub.availability.model.api.ActiveDealsResponse;
import com.kzhirov.eatclub.availability.service.DealService;

@RestController
@RequestMapping("/deals")
public class DealController {

    private final DateConverter dateConverter;
    private final DealService dealService;

    @Autowired
    public DealController(DealService dealService, DateConverter dateConverter) {
        this.dateConverter = dateConverter;
        this.dealService = dealService;
    }

    
    @RequestMapping("/active")
    public ActiveDealsResponse getActiveDeals(@RequestParam(required = true) String timeOfDay) {
        LocalTime time = dateConverter.convert(timeOfDay);
        return dealService.getActiveDeals(time);
    }
    
}
