package com.maksymchernenko.reserveit.repository;

import com.maksymchernenko.reserveit.model.RestaurantTable;

import java.util.List;
import java.util.Optional;

public interface RestaurantTableRepository {

    RestaurantTable save(RestaurantTable restaurantTable);
    Optional<RestaurantTable> get(long id);
    List<RestaurantTable> getTables(long restaurantId);
    List<RestaurantTable> getBySeatsNumber(long restaurantId, int minSeatsNumber);
    void delete(long restaurant);
    List<Integer> getTableNumbers(long restaurantId);
}
