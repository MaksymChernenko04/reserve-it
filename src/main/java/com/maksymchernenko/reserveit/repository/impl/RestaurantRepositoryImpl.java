package com.maksymchernenko.reserveit.repository.impl;

import com.maksymchernenko.reserveit.model.Restaurant;
import com.maksymchernenko.reserveit.repository.RestaurantRepository;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Implements {@link RestaurantRepository} interface using JPA and custom JPQL queries.
 */
@Repository
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantRepositoryImpl.class);

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
        logger.info("Creating restaurant = {}", restaurant);

        entityManager.persist(restaurant);

        return entityManager.find(Restaurant.class, restaurant.getId());
    }

    @Override
    public void update(Restaurant restaurant) {
        logger.info("Updating restaurant = {}", restaurant);

        entityManager.merge(restaurant);
    }

    @Override
    public void remove(long id) {
        logger.info("Deleting restaurant with id = {}", id);

        entityManager.remove(entityManager.find(Restaurant.class, id));
    }

    @Override
    public List<Restaurant> getAll() {
        logger.info("Fetching all restaurants");

        return entityManager.createQuery("FROM Restaurant", Restaurant.class).getResultList();
    }

    @Override
    public Optional<Restaurant> getRestaurant(long id) {
        logger.info("Fetching restaurant with id = {}", id);

        List<Restaurant> list = entityManager.createQuery("FROM Restaurant WHERE id = :id", Restaurant.class)
                .setParameter("id", id)
                .getResultList();

        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public Optional<Restaurant> getRestaurant(String name) {
        logger.info("Fetching restaurant with name = {}", name);

        List<Restaurant> list = entityManager.createQuery("FROM Restaurant WHERE name = :name", Restaurant.class)
                .setParameter("name", name)
                .getResultList();

        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }
}
