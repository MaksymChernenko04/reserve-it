package com.maksymchernenko.reserveit.exceptions;

import com.maksymchernenko.reserveit.model.User;

/**
 * Represents the exception occurring when the {@link User} already exists in the system.
 */
public class UserAlreadyExistsException extends Exception {

    /**
     * Instantiates a new User already exists exception.
     *
     * @param message the exception message
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
