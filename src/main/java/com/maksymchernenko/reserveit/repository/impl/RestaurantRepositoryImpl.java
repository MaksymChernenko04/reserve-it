package com.maksymchernenko.reserveit.repository.impl;

import com.maksymchernenko.reserveit.model.Restaurant;
import com.maksymchernenko.reserveit.repository.RestaurantRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Implements {@link RestaurantRepository} interface using JPA and custom JPQL queries.
 */
@Repository
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final EntityManager entityManager;

    /**
     * Instantiates a new {@link RestaurantRepository}.
     *
     * @param entityManager the entity manager
     */
    @Autowired
    public RestaurantRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        entityManager.persist(restaurant);

        return entityManager.find(Restaurant.class, restaurant.getId());
    }

    @Override
    public void update(Restaurant restaurant) {
        entityManager.merge(restaurant);
    }

    @Override
    public void remove(long id) {
        entityManager.remove(entityManager.find(Restaurant.class, id));
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
}
