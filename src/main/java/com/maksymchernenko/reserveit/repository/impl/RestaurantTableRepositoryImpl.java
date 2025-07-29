package com.maksymchernenko.reserveit.repository.impl;

import com.maksymchernenko.reserveit.model.RestaurantTable;
import com.maksymchernenko.reserveit.repository.RestaurantTableRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RestaurantTableRepositoryImpl implements RestaurantTableRepository {

    private final EntityManager entityManager;

    @Autowired
    public RestaurantTableRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public RestaurantTable save(RestaurantTable restaurantTable) {
        entityManager.persist(restaurantTable);

        return entityManager.find(RestaurantTable.class, restaurantTable.getId());
    }
}
