package com.kzhirov.eatclub.availability.model;

import java.time.LocalTime;

public record DealEvent(LocalTime time, boolean increase) {
    
}
