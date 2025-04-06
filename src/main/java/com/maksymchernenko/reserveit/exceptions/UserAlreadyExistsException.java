package com.maksymchernenko.reserveit.exceptions;

public class UserAlreadyExistsException extends Exception {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
