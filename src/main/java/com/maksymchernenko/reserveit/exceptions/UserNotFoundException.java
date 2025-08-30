package com.maksymchernenko.reserveit.exceptions;

import com.maksymchernenko.reserveit.model.User;

/**
 * Represents the exception occurring when the {@link User} is not found in the system.
 */
public class UserNotFoundException extends Exception {

    /**
     * Instantiates a new User not found exception.
     *
     * @param message the exception message
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
