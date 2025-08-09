package com.maksymchernenko.reserveit.repository;

import com.maksymchernenko.reserveit.model.Reservation;
import com.maksymchernenko.reserveit.model.RestaurantTable;
import com.maksymchernenko.reserveit.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    List<Reservation> getAll();
    List<Reservation> getByClient(User client);
    Optional<Reservation> get(long id);
    void reserve(RestaurantTable restaurantTable, User client, Reservation.Status status, LocalDateTime dateTime, int numberOfGuests);
    void cancelReservation(long id);
}
