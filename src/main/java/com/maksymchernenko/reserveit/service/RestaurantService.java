package com.maksymchernenko.reserveit.service;

import com.maksymchernenko.reserveit.exceptions.RestaurantAlreadyExistsException;
import com.maksymchernenko.reserveit.model.Restaurant;

import java.util.List;

public interface RestaurantService {

    List<Restaurant> getAllRestaurants();
    Restaurant getRestaurant(long id);
    Restaurant getRestaurant(String name);
    Restaurant createRestaurant(Restaurant restaurant) throws RestaurantAlreadyExistsException;
    void updateRestaurant(Restaurant restaurant);
    void deleteRestaurant(long id);
}
