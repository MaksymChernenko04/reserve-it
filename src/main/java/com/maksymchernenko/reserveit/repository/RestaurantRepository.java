package com.maksymchernenko.reserveit.repository;

import com.maksymchernenko.reserveit.model.Restaurant;
import com.maksymchernenko.reserveit.model.WorkingTime;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RestaurantRepository {

    List<Restaurant> getAll();
    Optional<Restaurant> getRestaurant(long id);
    Optional<Restaurant> getRestaurant(String name);
    List<WorkingTime> getWorkingTime(long id);
    Map<Integer, Integer> getTables(long id);
    Restaurant save(Restaurant restaurant);
    void remove(long id);
}
