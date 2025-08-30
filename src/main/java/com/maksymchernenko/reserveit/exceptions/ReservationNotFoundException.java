package com.maksymchernenko.reserveit.exceptions;

import com.maksymchernenko.reserveit.model.Reservation;

/**
 * Represents the exception occurring when the {@link Reservation} is not found in the system.
 */
public class ReservationNotFoundException extends RuntimeException {

    /**
     * Instantiates a new Reservation not found exception.
     *
     * @param message the exception message
     */
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
