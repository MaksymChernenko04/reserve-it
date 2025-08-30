package com.maksymchernenko.reserveit.repository;

import com.maksymchernenko.reserveit.model.Restaurant;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing and managing {@link Restaurant} entries in the database.
 * <p>
 * Provides CRUD methods.
 */
public interface RestaurantRepository {

    /**
     * Creates a new restaurant.
     *
     * @param restaurant the new restaurant
     * @return the saved restaurant
     */
    Restaurant save(Restaurant restaurant);

    /**
     * Updates a restaurant.
     *
     * @param restaurant the updated restaurant
     */
    void update(Restaurant restaurant);

    /**
     * Removes a restaurant by id.
     *
     * @param id the restaurant id
     */
    void remove(long id);

    /**
     * Gets all restaurants.
     *
     * @return the list of restaurants
     */
    List<Restaurant> getAll();

    /**
     * Gets a restaurant by id.
     *
     * @param id the restaurant id
     * @return the optional of a restaurant
     */
    Optional<Restaurant> getRestaurant(long id);

    /**
     * Gets a restaurant by name.
     *
     * @param name the restaurant name
     * @return the optional of a restaurant
     */
    Optional<Restaurant> getRestaurant(String name);
}
