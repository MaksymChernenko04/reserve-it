package com.maksymchernenko.reserveit.service;

import com.maksymchernenko.reserveit.model.RestaurantTable;

import java.util.Map;

public interface RestaurantTableService {
    RestaurantTable createTable(RestaurantTable restaurantTable);
    Map<Integer, Integer> getTableMap(long restaurantId);
    void deleteAll(long restaurantId);
}
