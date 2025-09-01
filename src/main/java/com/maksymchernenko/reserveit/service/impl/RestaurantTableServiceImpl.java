package com.maksymchernenko.reserveit.service.impl;

import com.maksymchernenko.reserveit.model.Reservation;
import com.maksymchernenko.reserveit.model.RestaurantTable;
import com.maksymchernenko.reserveit.repository.ReservationRepository;
import com.maksymchernenko.reserveit.repository.RestaurantTableRepository;
import com.maksymchernenko.reserveit.service.RestaurantTableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

/**
 * Implements {@link RestaurantTableService} interface.
 * <p>
 * Provides business logic methods to manage restaurant tables.
 */
@Service
public class RestaurantTableServiceImpl implements RestaurantTableService {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantTableServiceImpl.class);

    private final RestaurantTableRepository restaurantTableRepository;
    private final ReservationRepository reservationRepository;

    /**
     * Instantiates a new {@link RestaurantTableService}.
     *
     * @param restaurantTableRepository the {@link RestaurantTableRepository}
     * @param reservationRepository     the {@link ReservationRepository}
     */
    @Autowired
    public RestaurantTableServiceImpl(RestaurantTableRepository restaurantTableRepository,
                                      ReservationRepository reservationRepository) {
        this.restaurantTableRepository = restaurantTableRepository;
        this.reservationRepository = reservationRepository;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Searches for an available table number every time a new table is created.
     */
    @Transactional
    @Override
    public void createTable(RestaurantTable restaurantTable) {
        int n = 1;
        while (restaurantTableRepository.getTableNumbers(restaurantTable.getRestaurant().getId()).contains(n)) n++;
        restaurantTable.setNumber(n);

        logger.info("Creating table = {}", restaurantTable);

        restaurantTableRepository.save(restaurantTable);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The result list of tables is sorted by table number.
     */
    @Override
    public List<RestaurantTable> getTables(long restaurantId) {
        logger.info("Getting tables for restaurant with id = {}", restaurantId);

        List<RestaurantTable> tables = restaurantTableRepository.getTables(restaurantId);
        tables.sort(Comparator.comparing(RestaurantTable::getNumber));

        return tables;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deletes tables if they are not in the process of reserving.
     * <p>
     * Deletes the table in reservations.
     */
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

        logger.info("Deleting table with id = {}", id);

        reservationRepository.deleteTables(id);
        restaurantTableRepository.delete(id);

        return true;
    }
}
