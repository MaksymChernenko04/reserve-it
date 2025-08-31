package com.maksymchernenko.reserveit.repository.impl;

import com.maksymchernenko.reserveit.model.RestaurantTable;
import com.maksymchernenko.reserveit.repository.RestaurantTableRepository;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implements {@link RestaurantTableRepository} interface using JPA and custom JPQL queries.
 */
@Repository
public class RestaurantTableRepositoryImpl implements RestaurantTableRepository {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantTableRepositoryImpl.class);

    private final EntityManager entityManager;

    /**
     * Instantiates a new {@link RestaurantTableRepository}.
     *
     * @param entityManager the entity manager
     */
    @Autowired
    public RestaurantTableRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public RestaurantTable save(RestaurantTable restaurantTable) {
        if (restaurantTable.getId() == null) {
            logger.info("Creating restaurant table = {}", restaurantTable);

            entityManager.persist(restaurantTable);

            return restaurantTable;
        } else {
            logger.info("Updating restaurant table = {}", restaurantTable);

            return entityManager.merge(restaurantTable);
        }
    }

    @Override
    public Optional<RestaurantTable> get(long id) {
        logger.info("Fetching restaurant table with id = {}", id);

        List<RestaurantTable> table = entityManager.createQuery("FROM RestaurantTable WHERE id = :id", RestaurantTable.class)
                .setParameter("id", id)
                .getResultList();

        return table.isEmpty() ? Optional.empty() : Optional.of(table.get(0));
    }

    @Override
    public List<RestaurantTable> getTables(long restaurantId) {
        logger.info("Fetching restaurant tables with restaurant id = {}", restaurantId);

        return entityManager.createQuery("FROM RestaurantTable table WHERE table.restaurant.id = :id ", RestaurantTable.class)
                .setParameter("id", restaurantId)
                .getResultList();
    }

    @Override
    public List<RestaurantTable> getBySeatsNumber(long restaurantId,
                                                  int minSeatsNumber) {
        logger.info("Fetching restaurant tables with restaurant id = {}, minimum seats number = {}",
                restaurantId,
                minSeatsNumber);

        return entityManager.createQuery("FROM RestaurantTable WHERE restaurant.id = :restaurantId " +
                        "AND seatsNumber >= :minSeatsNumber", RestaurantTable.class)
                .setParameter("restaurantId", restaurantId)
                .setParameter("minSeatsNumber", minSeatsNumber)
                .getResultList();
    }

    @Override
    public void delete(long id) {
        logger.info("Deleting restaurant table with id = {}", id);

        entityManager.createQuery("DELETE FROM RestaurantTable WHERE id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public List<Integer> getTableNumbers(long restaurantId) {
        logger.info("Fetching restaurant table numbers with restaurant id = {}", restaurantId);

        return entityManager.createQuery("SELECT number FROM RestaurantTable WHERE restaurant.id = :id", Integer.class)
                .setParameter("id", restaurantId)
                .getResultList();
    }
}
