package com.kzhirov.eatclub.availability.model.api;

import java.util.List;

public record ActiveDealsResponse (List<DealApiModel> deals) { }