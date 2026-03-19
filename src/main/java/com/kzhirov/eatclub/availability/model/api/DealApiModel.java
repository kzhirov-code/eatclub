package com.kzhirov.eatclub.availability.model.api;

public record DealApiModel (String restaurantObjectId, String restaurantName, String restaurantAddress1, String restaurantSuburb, 
      String restaurantOpen, String restaurantClose, String dealObjectId, String discount, String dineIn, String lightning, String qtyLeft) { }