package com.kzhirov.eatclub.availability.converter;

import org.springframework.stereotype.Component;

import com.kzhirov.eatclub.availability.model.api.DealApiModel;
import com.kzhirov.eatclub.availability.model.dto.DealDto;
import com.kzhirov.eatclub.availability.model.dto.RestaurantDto;

@Component
public class DealConverter {
    public DealApiModel convert(RestaurantDto restaurantDto, DealDto dealDto) {
        DealApiModel dealApiModel = new DealApiModel(restaurantDto.objectId(), restaurantDto.name(), restaurantDto.address1(), restaurantDto.suburb(), restaurantDto.open(),
        restaurantDto.close(), dealDto.objectId(), dealDto.discount(), dealDto.dineIn(), dealDto.lightning(), dealDto.qtyLeft());
        return dealApiModel;
    }
}
