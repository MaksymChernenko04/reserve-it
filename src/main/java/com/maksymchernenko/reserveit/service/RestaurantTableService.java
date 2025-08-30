package com.maksymchernenko.reserveit.service;

import com.maksymchernenko.reserveit.model.Reservation;
import com.maksymchernenko.reserveit.model.Restaurant;
import com.maksymchernenko.reserveit.model.RestaurantTable;

import java.util.List;

/**
 * Service interface for managing {@link Reservation} objects.
 * <p>
 * Defines business logic methods to reserve tables, cancel, submit, update and get reservations.
 */
public interface RestaurantTableService {

    /**
     * Creates a new restaurant table.
     *
     * @param restaurantTable the new restaurant table
     */
    void createTable(RestaurantTable restaurantTable);

    /**
     * Gets tables by {@link Restaurant} id.
     *
     * @param restaurantId the restaurant id
     * @return the list of tables
     */
    List<RestaurantTable> getTables(long restaurantId);

    /**
     * Deletes a restaurant table.
     *
     * @param id the restaurant table id
     * @return {@code true} if deleted successfully, {@code false} otherwise
     */
    boolean delete(long id);
}
