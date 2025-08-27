package com.maksymchernenko.reserveit.repository;

import com.maksymchernenko.reserveit.model.Reservation;
import com.maksymchernenko.reserveit.model.RestaurantTable;
import com.maksymchernenko.reserveit.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    List<Reservation> getAll();
    List<Reservation> getByClientAndStatuses(User client, List<Reservation.Status> statuses);
    Optional<Reservation> get(long id);
    List<Reservation> getByTableId(long tableId);
    void reserve(RestaurantTable restaurantTable, User client, Reservation.Status status, LocalDateTime dateTime, int numberOfGuests);
    void update(Reservation reservation);
    void cancelReservation(long id);
    void deleteTables(long tableId);
}
