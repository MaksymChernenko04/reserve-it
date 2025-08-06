package com.maksymchernenko.reserveit.service.impl;

import com.maksymchernenko.reserveit.model.RestaurantTable;
import com.maksymchernenko.reserveit.repository.RestaurantTableRepository;
import com.maksymchernenko.reserveit.service.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

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

    @Override
    public Map<Integer, Integer> getTableMap(long restaurantId) {
        return restaurantTableRepository.getTableMap(restaurantId);
    }

    @Transactional
    @Override
    public void deleteAll(long restaurantId) {
        restaurantTableRepository.deleteAll(restaurantId);
    }
}
