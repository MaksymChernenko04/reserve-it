package com.maksymchernenko.reserveit.service.impl;

import com.maksymchernenko.reserveit.exceptions.RestaurantAlreadyExistsException;
import com.maksymchernenko.reserveit.exceptions.RestaurantNotFoundException;
import com.maksymchernenko.reserveit.model.Restaurant;
import com.maksymchernenko.reserveit.repository.RestaurantRepository;
import com.maksymchernenko.reserveit.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implements {@link RestaurantService} interface.
 * <p>
 * Provides business logic methods to manage reservations.
 */
@Service
public class RestaurantServiceImpl implements RestaurantService {

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
        return restaurantRepository.getAll();
    }

    @Override
    public Restaurant getRestaurant(long id) throws RestaurantNotFoundException {
        return restaurantRepository.getRestaurant(id).orElseThrow(() -> new RestaurantNotFoundException("Restaurant with given name does not exists"));
    }

    @Override
    public Restaurant getRestaurant(String name) throws RestaurantNotFoundException {
        return restaurantRepository.getRestaurant(name).orElseThrow(() -> new RestaurantNotFoundException("Restaurant with given name does not exists"));
    }

    @Transactional
    @Override
    public Restaurant createRestaurant(Restaurant restaurant) throws RestaurantAlreadyExistsException {
        if (restaurantRepository.getRestaurant(restaurant.getName()).isPresent()) {
            throw new RestaurantAlreadyExistsException("Restaurant with given name already exists");
        } else {
            return restaurantRepository.save(restaurant);
        }
    }

    @Transactional
    @Override
    public void updateRestaurant(Restaurant restaurant) {
        restaurantRepository.update(restaurant);
    }

    @Transactional
    @Override
    public void deleteRestaurant(long id) {
        restaurantRepository.remove(id);
    }
}
