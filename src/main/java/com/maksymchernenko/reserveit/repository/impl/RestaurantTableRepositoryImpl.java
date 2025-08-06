package com.maksymchernenko.reserveit.repository.impl;

import com.maksymchernenko.reserveit.model.RestaurantTable;
import com.maksymchernenko.reserveit.repository.RestaurantTableRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public Map<Integer, Integer> getTableMap(long restaurantId) {
        List<Object[]> rows = entityManager.createQuery("SELECT table.seatsNumber, COUNT(table) " +
                        "FROM RestaurantTable table " +
                        "WHERE table.restaurant.id = :id " +
                        "GROUP BY table.seatsNumber", Object[].class)
                .setParameter("id", restaurantId)
                .getResultList();

        Map<Integer,Integer> result = new LinkedHashMap<>();
        for (Object[] row : rows) {
            Integer seatsNumber = (Integer) row[0];
            Long countLong = (Long) row[1];
            result.put(seatsNumber, countLong.intValue());
        }

        return result;
    }

    @Override
    public void deleteAll(long restaurantId) {
        entityManager.createQuery("DELETE FROM RestaurantTable WHERE restaurant.id = :id")
                .setParameter("id", restaurantId)
                .executeUpdate();
    }
}
