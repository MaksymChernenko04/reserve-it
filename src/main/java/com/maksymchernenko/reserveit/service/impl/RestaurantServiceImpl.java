package com.maksymchernenko.reserveit.service.impl;

import com.maksymchernenko.reserveit.exceptions.RestaurantAlreadyExistsException;
import com.maksymchernenko.reserveit.exceptions.RestaurantNotFoundException;
import com.maksymchernenko.reserveit.model.Restaurant;
import com.maksymchernenko.reserveit.repository.RestaurantRepository;
import com.maksymchernenko.reserveit.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implements {@link RestaurantService} interface.
 * <p>
 * Provides business logic methods to manage reservations.
 */
@Service
public class RestaurantServiceImpl implements RestaurantService {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantServiceImpl.class);

    private final RestaurantRepository restaurantRepository;

    /**
     * Instantiates a new {@link RestaurantService}.
     *
     * @param restaurantRepository the {@link RestaurantRepository}
     */
    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<Restaurant> getAllRestaurants() {
        logger.info("Getting all restaurants");

        return restaurantRepository.getAll();
    }

    @Override
    public Restaurant getRestaurant(long id) throws RestaurantNotFoundException {
        logger.info("Getting restaurant with id {}", id);

        Optional<Restaurant> restaurant = restaurantRepository.getRestaurant(id);

        if (restaurant.isEmpty()) {
            logger.warn("Restaurant with id = {} does not exist", id);

            throw new RestaurantNotFoundException("Restaurant with given id does not exists");
        }

        return restaurant.get();
    }

    @Override
    public Restaurant getRestaurant(String name) throws RestaurantNotFoundException {
        logger.info("Getting restaurant with name {}", name);

        Optional<Restaurant> restaurant = restaurantRepository.getRestaurant(name);

        if (restaurant.isEmpty()) {
            logger.warn("Restaurant with name = {} does not exist", name);

            throw new RestaurantNotFoundException("Restaurant with given name does not exists");
        }

        return restaurant.get();
    }

    @Transactional
    @Override
    public Restaurant createRestaurant(Restaurant restaurant) throws RestaurantAlreadyExistsException {
        if (restaurantRepository.getRestaurant(restaurant.getName()).isPresent()) {
            logger.warn("Restaurant with name = {} already exists", restaurant.getName());

            throw new RestaurantAlreadyExistsException("Restaurant with given name already exists");
        } else {
            logger.info("Creating restaurant {}", restaurant);

            return restaurantRepository.save(restaurant);
        }
    }

    @Transactional
    @Override
    public void updateRestaurant(Restaurant restaurant) {
        logger.info("Updating restaurant {}", restaurant);

        restaurantRepository.update(restaurant);
    }

    @Transactional
    @Override
    public void deleteRestaurant(long id) {
        logger.info("Deleting restaurant with id = {}", id);

        restaurantRepository.remove(id);
    }
}
