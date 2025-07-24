package com.maksymchernenko.reserveit.service;

import com.maksymchernenko.reserveit.exceptions.RestaurantAlreadyExistsException;
import com.maksymchernenko.reserveit.model.Restaurant;
import com.maksymchernenko.reserveit.model.WorkingTime;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

public interface RestaurantService {

    List<Restaurant> getAllRestaurants();
    Restaurant getRestaurant(long id);
    Restaurant getRestaurant(String name);
    Map<DayOfWeek, WorkingTime> getWorkingTime(long id);
    Restaurant createRestaurant(Restaurant restaurant) throws RestaurantAlreadyExistsException;
    void deleteRestaurant(long id);
}
