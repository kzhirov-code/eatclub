package com.kzhirov.eatclub.availability.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.kzhirov.eatclub.availability.model.api.DealApiModel;
import com.kzhirov.eatclub.availability.model.dto.DealDto;
import com.kzhirov.eatclub.availability.model.dto.RestaurantDto;

public class DealConverterTest {
    @Test
    void testConvert() {
        DealConverter dealConverter = new DealConverter();
        RestaurantDto restaurant = new RestaurantDto(
                "tavern-prancing-pony",
                "Prancing Pony",
                "crn Great Easter Road and Greenway",
                "Bree",
                "7:00am",
                "11:00pm",
                List.of());
        DealDto deal = new DealDto(
                "halflings-half-price",
                "50",
                "true",
                "false",
                null,
                null,
                "4");

        DealApiModel dealApiModel = dealConverter.convert(restaurant, deal);
        assertEquals(dealApiModel.restaurantObjectId(), restaurant.objectId());
        assertEquals(dealApiModel.restaurantName(), restaurant.name());
        assertEquals(dealApiModel.restaurantAddress1(), restaurant.address1());
        assertEquals(dealApiModel.restaurantSuburb(), restaurant.suburb());
        assertEquals(dealApiModel.restaurantOpen(), restaurant.open());
        assertEquals(dealApiModel.restaurantClose(), restaurant.close());
        assertEquals(dealApiModel.dealObjectId(), deal.objectId());
        assertEquals(dealApiModel.discount(), deal.discount());
        assertEquals(dealApiModel.dineIn(), deal.dineIn());
        assertEquals(dealApiModel.lightning(), deal.lightning());
        assertEquals(dealApiModel.qtyLeft(), deal.qtyLeft());
    }
}
