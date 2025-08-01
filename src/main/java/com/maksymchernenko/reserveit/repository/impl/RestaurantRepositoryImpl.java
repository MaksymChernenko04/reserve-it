package com.maksymchernenko.reserveit.repository.impl;

import com.maksymchernenko.reserveit.model.Restaurant;
import com.maksymchernenko.reserveit.model.WorkingTime;
import com.maksymchernenko.reserveit.repository.RestaurantRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final EntityManager entityManager;

    @Autowired
    public RestaurantRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Restaurant> getAll() {
        return entityManager.createQuery("FROM Restaurant", Restaurant.class).getResultList();
    }

    @Override
    public Optional<Restaurant> getRestaurant(long id) {
        List<Restaurant> list = entityManager.createQuery("FROM Restaurant WHERE id = :id", Restaurant.class)
                .setParameter("id", id)
                .getResultList();

        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public Optional<Restaurant> getRestaurant(String name) {
        List<Restaurant> list = entityManager.createQuery("FROM Restaurant WHERE name = :name", Restaurant.class)
                .setParameter("name", name)
                .getResultList();

        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public List<WorkingTime> getWorkingTime(long id) {
        return entityManager.createQuery("FROM WorkingTime WHERE restaurant.id = :id", WorkingTime.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public Map<Integer, Integer> getTables(long id) {
        List<Object[]> rows = entityManager.createQuery("SELECT table.seatsNumber, COUNT(table) " +
                        "FROM RestaurantTable table " +
                        "WHERE table.restaurant.id = :id " +
                        "GROUP BY table.seatsNumber", Object[].class)
                .setParameter("id", id)
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
    public Restaurant save(Restaurant restaurant) {
        entityManager.persist(restaurant);

        return entityManager.find(Restaurant.class, restaurant.getId());
    }

    @Override
    public void remove(long id) {
        entityManager.remove(entityManager.find(Restaurant.class, id));
    }
}
