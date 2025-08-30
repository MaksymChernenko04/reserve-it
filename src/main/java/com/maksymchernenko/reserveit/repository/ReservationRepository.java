package com.maksymchernenko.reserveit.repository;

import com.maksymchernenko.reserveit.model.Reservation;
import com.maksymchernenko.reserveit.model.RestaurantTable;
import com.maksymchernenko.reserveit.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing and managing {@link Reservation} entries in the database.
 * <p>
 * Provides methods to create, update, cancel, query reservations and dissociate tables from existing reservations.
 */
public interface ReservationRepository {

    /**
     * Gets all reservations.
     *
     * @return the list of reservations
     */
    List<Reservation> getAll();

    /**
     * Gets reservations by {@link User} (client) where the {@link Reservation.Status}
     * matches any value from the provided list.
     *
     * @param client   the client associated with a reservation
     * @param statuses the list of statuses
     * @return the list of reservations
     */
    List<Reservation> getByClientAndStatuses(User client,
                                             List<Reservation.Status> statuses);

    /**
     * Gets a reservation by id.
     *
     * @param id the reservation id
     * @return the optional of a reservation
     */
    Optional<Reservation> get(long id);

    /**
     * Gets reservations by associated {@link RestaurantTable} id.
     *
     * @param tableId the restaurant table id
     * @return the list of reservations
     */
    List<Reservation> getByTableId(long tableId);

    /**
     * Creates a new reservation for a given {@link User} (client) and {@link RestaurantTable}.
     *
     * @param restaurantTable the restaurant table to be reserved
     * @param client          the client making the reservation
     * @param status          the initial reservation status
     * @param dateTime        the reservation date and time
     * @param numberOfGuests  the number of guests in the reservation
     */
    void reserve(RestaurantTable restaurantTable,
                 User client,
                 Reservation.Status status,
                 LocalDateTime dateTime,
                 int numberOfGuests);

    /**
     * Updates an existing reservation.
     *
     * @param reservation the updated reservation
     */
    void update(Reservation reservation);

    /**
     * Cancels a reservation by updating its {@link Reservation.Status}.
     * <p>
     * The reservation is not deleted from the database.
     *
     * @param id the reservation id
     */
    void cancelReservation(long id);

    /**
     * Removes the association of reservations with a {@link RestaurantTable}.
     *
     * @param tableId the restaurant table id
     */
    void deleteTables(long tableId);
}
