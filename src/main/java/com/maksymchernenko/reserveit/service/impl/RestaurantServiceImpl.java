package com.maksymchernenko.reserveit.service.impl;

import com.maksymchernenko.reserveit.exceptions.RestaurantAlreadyExistsException;
import com.maksymchernenko.reserveit.model.Restaurant;
import com.maksymchernenko.reserveit.repository.RestaurantRepository;
import com.maksymchernenko.reserveit.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.getAll();
    }

    @Override
    public Restaurant getRestaurant(long id) {
        return restaurantRepository.getRestaurant(id).orElseGet(Restaurant::new);
    }

    @Override
    public Restaurant getRestaurant(String name) {
        return restaurantRepository.getRestaurant(name).orElseGet(Restaurant::new);
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
    public Restaurant updateRestaurant(Restaurant restaurant) {
        return restaurantRepository.update(restaurant);
    }

    @Transactional
    @Override
    public void deleteRestaurant(long id) {
        restaurantRepository.remove(id);
    }
}
