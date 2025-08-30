package com.maksymchernenko.reserveit.service;

import com.maksymchernenko.reserveit.exceptions.RestaurantAlreadyExistsException;
import com.maksymchernenko.reserveit.exceptions.RestaurantNotFoundException;
import com.maksymchernenko.reserveit.model.Restaurant;

import java.util.List;

/**
 * Service interface for managing {@link Restaurant} objects.
 * <p>
 * Defines business logic methods to manage restaurants.
 */
public interface RestaurantService {

    /**
     * Gets all restaurants.
     *
     * @return the list of restaurants
     */
    List<Restaurant> getAllRestaurants();

    /**
     * Gets a restaurant by id.
     *
     * @param id the restaurant id
     * @return the restaurant
     * @throws RestaurantNotFoundException if the restaurant with a specified name is not found
     */
    Restaurant getRestaurant(long id) throws RestaurantNotFoundException;

    /**
     * Gets a restaurant by name.
     *
     * @param name the restaurant name
     * @return the restaurant
     * @throws RestaurantNotFoundException if the restaurant with a specified name is not found
     */
    Restaurant getRestaurant(String name) throws RestaurantNotFoundException;

    /**
     * Creates a new restaurant.
     *
     * @param restaurant the new restaurant
     * @return the saved restaurant
     * @throws RestaurantAlreadyExistsException if the restaurant with a specified name already exists
     */
    Restaurant createRestaurant(Restaurant restaurant) throws RestaurantAlreadyExistsException;

    /**
     * Updates a restaurant.
     *
     * @param restaurant the updated restaurant
     */
    void updateRestaurant(Restaurant restaurant);

    /**
     * Deletes a restaurant.
     *
     * @param id the restaurant id
     */
    void deleteRestaurant(long id);
}
