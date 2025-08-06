package com.maksymchernenko.reserveit.repository;

import com.maksymchernenko.reserveit.model.RestaurantTable;

import java.util.Map;

public interface RestaurantTableRepository {

    RestaurantTable save(RestaurantTable restaurantTable);
    Map<Integer, Integer> getTableMap(long restaurantId);
    void deleteAll(long restaurantId);
}
