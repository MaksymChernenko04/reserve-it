package com.maksymchernenko.reserveit.service.impl;

import com.maksymchernenko.reserveit.model.Reservation;
import com.maksymchernenko.reserveit.model.RestaurantTable;
import com.maksymchernenko.reserveit.repository.ReservationRepository;
import com.maksymchernenko.reserveit.repository.RestaurantTableRepository;
import com.maksymchernenko.reserveit.service.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class RestaurantTableServiceImpl implements RestaurantTableService {

    private final RestaurantTableRepository restaurantTableRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public RestaurantTableServiceImpl(RestaurantTableRepository restaurantTableRepository,
                                      ReservationRepository reservationRepository) {
        this.restaurantTableRepository = restaurantTableRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    @Override
    public void createTable(RestaurantTable restaurantTable) {
        int n = 1;
        while (restaurantTableRepository.getTableNumbers(restaurantTable.getRestaurant().getId()).contains(n)) n++;
        restaurantTable.setNumber(n);

        restaurantTableRepository.save(restaurantTable);
    }

    @Override
    public List<RestaurantTable> getTables(long restaurantId) {
        List<RestaurantTable> tables = restaurantTableRepository.getTables(restaurantId);
        tables.sort(Comparator.comparing(RestaurantTable::getNumber));

        return tables;
    }

    @Transactional
    @Override
    public boolean delete(long id) {
        List<Reservation> reservations = reservationRepository.getByTableId(id);

        for (Reservation reservation : reservations) {
            if (reservation.getStatus() == Reservation.Status.PENDING
                    || reservation.getStatus() == Reservation.Status.RESERVED) {
                return false;
            }
        }

        reservationRepository.deleteTables(id);
        restaurantTableRepository.delete(id);

        return true;
    }
}
