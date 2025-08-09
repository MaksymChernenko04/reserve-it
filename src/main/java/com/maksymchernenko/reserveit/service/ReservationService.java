package com.maksymchernenko.reserveit.service;

import com.maksymchernenko.reserveit.model.Reservation;
import com.maksymchernenko.reserveit.model.RestaurantTable;
import com.maksymchernenko.reserveit.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ReservationService {
    List<Reservation> getByClient(User client);
    Map<RestaurantTable, List<LocalDateTime>> getAvailableTablesMap(long restaurantId, int numberOfGuests);
    Reservation getReservation(long id);
    boolean reserve(long restaurantId, LocalDateTime dateTime, int numberOfGuests, User client);
    void cancelReservation(long id);
}
