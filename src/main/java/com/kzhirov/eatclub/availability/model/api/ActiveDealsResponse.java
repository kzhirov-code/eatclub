package com.kzhirov.eatclub.availability.model.api;

import java.util.List;

public class ActiveDealsResponse {    
    private List<DealApiModel> deals;

    public List<DealApiModel> getDeals() {
        return deals;
    }
    public void setDeals(List<DealApiModel> deals) {
        this.deals = deals;
    }
}
