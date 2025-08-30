package com.maksymchernenko.reserveit.repository;

import com.maksymchernenko.reserveit.model.RestaurantTable;
import com.maksymchernenko.reserveit.model.Restaurant;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing and managing {@link RestaurantTable} entries in the database.
 * <p>
 * Provides methods to save, delete and query restaurant tables.
 */
public interface RestaurantTableRepository {

    /**
     * Saves a restaurant table.
     *
     * @param restaurantTable the restaurant table
     * @return the saved restaurant table
     */
    RestaurantTable save(RestaurantTable restaurantTable);

    /**
     * Gets a restaurant table by id.
     *
     * @param id the restaurant table id
     * @return the optional of restaurant table
     */
    Optional<RestaurantTable> get(long id);

    /**
     * Gets restaurant tables by {@link Restaurant} id.
     *
     * @param restaurantId the restaurant id
     * @return the list of tables
     */
    List<RestaurantTable> getTables(long restaurantId);

    /**
     * Gets restaurant tables by the minimum number of seats.
     *
     * @param restaurantId   the restaurant id
     * @param minSeatsNumber the minimum number of seats
     * @return the list of tables
     */
    List<RestaurantTable> getBySeatsNumber(long restaurantId,
                                           int minSeatsNumber);

    /**
     * Deletes a restaurant table by id.
     *
     * @param id the restaurant table id
     */
    void delete(long id);

    /**
     * Gets table numbers.
     *
     * @param restaurantId the restaurant id
     * @return the list of table numbers
     */
    List<Integer> getTableNumbers(long restaurantId);
}
