package com.maksymchernenko.reserveit.service;

import com.maksymchernenko.reserveit.model.Reservation;
import com.maksymchernenko.reserveit.model.Restaurant;
import com.maksymchernenko.reserveit.model.RestaurantTable;
import com.maksymchernenko.reserveit.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Service interface for managing {@link Reservation} objects.
 * <p>
 * Defines business logic methods to reserve tables, cancel, submit, update and get reservations.
 */
public interface ReservationService {

    /**
     * Defines the number of days available for reservations from today.
     */
    int AVAILABLE_DAYS_FOR_RESERVATION = 3;
    /**
     * Defines the reservation duration of hours.
     */
    int RESERVATION_DURATION_OF_HOURS = 2;
    /**
     * Defines the interval of minutes for available reservation times.
     */
    int MINUTES_INTERVAL = 15;

    /**
     * Gets actual reservations for a given {@link User} (client).
     *
     * @param client the client
     * @return the list of reservations
     */
    List<Reservation> getActualByClient(User client);

    /**
     * Gets a reservation history for a given {@link User} (client).
     *
     * @param client the client
     * @return the list of reservations
     */
    List<Reservation> getHistoryByClient(User client);

    /**
     * Gets all reservations sorted by a specified filter.
     *
     * @param filter  the filter
     * @param manager the manager
     * @return the list of reservations
     */
    List<Reservation> getAll(String filter,
                             User manager);

    /**
     * Gets available {@link RestaurantTable} objects with times
     * by a specified {@link Restaurant} id and a number of guests.
     *
     * @param restaurantId   the restaurant id
     * @param numberOfGuests the number of guests
     * @return the available tables map
     */
    Map<RestaurantTable, List<LocalDateTime>> getAvailableTablesMap(long restaurantId,
                                                                    int numberOfGuests);

    /**
     * Gets reservation by id.
     *
     * @param id the reservation id
     * @return the reservation
     */
    Reservation getReservation(long id);

    /**
     * Reserves a {@link RestaurantTable} for a specified {@link Restaurant} id,
     * {@link User} (client), number of guests and time.
     *
     * @param restaurantId   the restaurant id
     * @param dateTime       the reservation date and time
     * @param numberOfGuests the number of guests
     * @param client         the client making the reservation
     * @return {@code true} if reserved successfully, {@code false} otherwise
     */
    boolean reserve(long restaurantId,
                    LocalDateTime dateTime,
                    int numberOfGuests,
                    User client);

    /**
     * Updates a reservation.
     *
     * @param reservation the updated reservation
     * @return {@code true} if updated successfully, {@code false} otherwise
     */
    boolean updateReservation(Reservation reservation);

    /**
     * Cancels a reservation.
     *
     * @param id the reservation id
     */
    void cancelReservation(long id);

    /**
     * Submits a reservation.
     *
     * @param id      the reservation id
     * @param manager the responsible manager
     */
    void submitReservation(long id,
                           User manager);
}
