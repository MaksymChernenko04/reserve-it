package com.maksymchernenko.reserveit.repository;

import com.maksymchernenko.reserveit.model.Reservation;
import com.maksymchernenko.reserveit.model.RestaurantTable;
import com.maksymchernenko.reserveit.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository {
    List<Reservation> getAll();
    List<Reservation> getByClient(User client);

    void reserve(RestaurantTable restaurantTable, User client, Reservation.Status status, LocalDateTime dateTime, int numberOfGuests);
}
