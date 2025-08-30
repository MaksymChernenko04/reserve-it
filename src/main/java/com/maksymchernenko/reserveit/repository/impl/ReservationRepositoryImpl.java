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
import java.util.Optional;

/**
 * Implements {@link ReservationRepository} interface using JPA and custom JPQL queries.
 */
@Repository
public class ReservationRepositoryImpl implements ReservationRepository {

    private final EntityManager entityManager;

    /**
     * Instantiates a new {@link ReservationRepository}.
     *
     * @param entityManager the entity manager
     */
    @Autowired
    public ReservationRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Reservation> getAll() {
        return entityManager.createQuery("FROM Reservation", Reservation.class)
                .getResultList();
    }

    @Override
    public List<Reservation> getByClientAndStatuses(User client,
                                                    List<Reservation.Status> statuses) {
        return entityManager.createQuery("FROM Reservation WHERE client.id = :id AND status IN :statuses", Reservation.class)
                .setParameter("id", client.getId())
                .setParameter("statuses", statuses)
                .getResultList();
    }

    @Override
    public Optional<Reservation> get(long id) {
        List<Reservation> reservation = entityManager.createQuery("FROM Reservation WHERE id = :id", Reservation.class)
                .setParameter("id", id)
                .getResultList();

        return reservation.isEmpty() ? Optional.empty() : Optional.of(reservation.get(0));
    }

    @Override
    public List<Reservation> getByTableId(long tableId) {
        return entityManager.createQuery("FROM Reservation WHERE table.id = :id", Reservation.class)
                .setParameter("id", tableId)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Sets responsible manager to {@code null}.
     */
    @Override
    public void reserve(RestaurantTable restaurantTable,
                        User client,
                        Reservation.Status status,
                        LocalDateTime dateTime,
                        int numberOfGuests) {
        entityManager.persist(new Reservation(restaurantTable, client, null, status, dateTime, numberOfGuests));
    }

    @Override
    public void update(Reservation reservation) {
        entityManager.merge(reservation);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Sets {@link Reservation.Status} to {@link Reservation.Status#CANCELED}.
     */
    @Override
    public void cancelReservation(long id) {
        entityManager.createQuery("UPDATE Reservation r SET r.status = :status WHERE id = :id")
                .setParameter("status", Reservation.Status.CANCELED)
                .setParameter("id", id)
                .executeUpdate();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Sets {@link RestaurantTable} field to {@code null} for reservations with a specified {@link RestaurantTable} id.
     */
    @Override
    public void deleteTables(long tableId) {
        entityManager.createQuery("UPDATE Reservation SET table = null WHERE table.id = :id")
                .setParameter("id", tableId)
                .executeUpdate();
    }
}
