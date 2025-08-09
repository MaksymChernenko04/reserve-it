package com.maksymchernenko.reserveit.repository.impl;

import com.maksymchernenko.reserveit.model.Reservation;
import com.maksymchernenko.reserveit.model.RestaurantTable;
import com.maksymchernenko.reserveit.model.User;
import com.maksymchernenko.reserveit.repository.ReservationRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository {

    private final EntityManager entityManager;

    @Autowired
    public ReservationRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Reservation> getAll() {
        return entityManager.createQuery("FROM Reservation", Reservation.class).getResultList();
    }

    @Override
    public List<Reservation> getByClient(User client) {
        return entityManager.createQuery("FROM Reservation WHERE client.id = :id", Reservation.class)
                .setParameter("id", client.getId())
                .getResultList();
    }

    @Override
    public void reserve(RestaurantTable restaurantTable,
                           User client,
                           Reservation.Status status,
                           LocalDateTime dateTime,
                           int numberOfGuests) {
        entityManager.persist(new Reservation(restaurantTable, client, null, status, dateTime, numberOfGuests));
    }
}
