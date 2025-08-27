package com.maksymchernenko.reserveit.repository.impl;

import com.maksymchernenko.reserveit.model.RestaurantTable;
import com.maksymchernenko.reserveit.repository.RestaurantTableRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RestaurantTableRepositoryImpl implements RestaurantTableRepository {

    private final EntityManager entityManager;

    @Autowired
    public RestaurantTableRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public RestaurantTable save(RestaurantTable restaurantTable) {
        if (restaurantTable.getId() == null) {
            entityManager.persist(restaurantTable);

            return restaurantTable;
        } else {
            return entityManager.merge(restaurantTable);
        }
    }

    @Override
    public Optional<RestaurantTable> get(long id) {
        List<RestaurantTable> table = entityManager.createQuery("FROM RestaurantTable WHERE id = :id", RestaurantTable.class)
                .setParameter("id", id)
                .getResultList();

        return table.isEmpty() ? Optional.empty() : Optional.of(table.get(0));
    }

    @Override
    public List<RestaurantTable> getTables(long restaurantId) {
        return entityManager.createQuery("FROM RestaurantTable table WHERE table.restaurant.id = :id ", RestaurantTable.class)
                .setParameter("id", restaurantId)
                .getResultList();
    }

    @Override
    public List<RestaurantTable> getBySeatsNumber(long restaurantId,
                                                  int minSeatsNumber) {
        return entityManager.createQuery("FROM RestaurantTable WHERE restaurant.id = :restaurantId " +
                        "AND seatsNumber >= :minSeatsNumber", RestaurantTable.class)
                .setParameter("restaurantId", restaurantId)
                .setParameter("minSeatsNumber", minSeatsNumber)
                .getResultList();
    }

    @Override
    public void delete(long id) {
        entityManager.createQuery("DELETE FROM RestaurantTable WHERE id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public List<Integer> getTableNumbers(long restaurantId) {
        return entityManager.createQuery("SELECT number FROM RestaurantTable WHERE restaurant.id = :id", Integer.class)
                .setParameter("id", restaurantId)
                .getResultList();
    }
}
