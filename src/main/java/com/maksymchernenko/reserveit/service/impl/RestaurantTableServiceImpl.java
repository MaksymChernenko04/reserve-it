package com.maksymchernenko.reserveit.service.impl;

import com.maksymchernenko.reserveit.model.RestaurantTable;
import com.maksymchernenko.reserveit.repository.RestaurantTableRepository;
import com.maksymchernenko.reserveit.service.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RestaurantTableServiceImpl implements RestaurantTableService {

    private final RestaurantTableRepository restaurantTableRepository;

    @Autowired
    public RestaurantTableServiceImpl(RestaurantTableRepository restaurantTableRepository) {
        this.restaurantTableRepository = restaurantTableRepository;
    }

    @Transactional
    @Override
    public RestaurantTable createTable(RestaurantTable restaurantTable) {
        return restaurantTableRepository.save(restaurantTable);
    }
}
