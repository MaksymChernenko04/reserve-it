package com.maksymchernenko.reserveit.service;

import com.maksymchernenko.reserveit.model.RestaurantTable;

import java.util.List;

public interface RestaurantTableService {

    void createTable(RestaurantTable restaurantTable);
    List<RestaurantTable> getTables(long restaurantId);
    boolean delete(long id);
}
