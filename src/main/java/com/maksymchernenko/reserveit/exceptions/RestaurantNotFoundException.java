package com.maksymchernenko.reserveit.exceptions;

import com.maksymchernenko.reserveit.model.Restaurant;

/**
 * Represents the exception occurring when the {@link Restaurant} is not found in the system.
 */
public class RestaurantNotFoundException extends RuntimeException {

    /**
     * Instantiates a new Restaurant not found exception.
     *
     * @param message the exception message
     */
    public RestaurantNotFoundException(String message) {
        super(message);
    }
}
