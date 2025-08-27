package com.maksymchernenko.reserveit.repository;

import com.maksymchernenko.reserveit.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository {

    Restaurant save(Restaurant restaurant);
    void update(Restaurant restaurant);
    void remove(long id);
    List<Restaurant> getAll();
    Optional<Restaurant> getRestaurant(long id);
    Optional<Restaurant> getRestaurant(String name);
}
