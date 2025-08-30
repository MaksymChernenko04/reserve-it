package com.maksymchernenko.reserveit.exceptions;

import com.maksymchernenko.reserveit.model.Restaurant;

/**
 * Represents the exception occurring when the {@link Restaurant} already exists in the system.
 */
public class RestaurantAlreadyExistsException extends Exception {

    /**
     * Instantiates a new Restaurant already exists exception.
     *
     * @param message the exception message
     */
    public RestaurantAlreadyExistsException(String message) {
        super(message);
    }
}
