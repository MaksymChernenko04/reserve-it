package com.maksymchernenko.reserveit.service.impl;

import com.maksymchernenko.reserveit.exceptions.ReservationNotFoundException;
import com.maksymchernenko.reserveit.model.Reservation;
import com.maksymchernenko.reserveit.model.RestaurantTable;
import com.maksymchernenko.reserveit.model.User;
import com.maksymchernenko.reserveit.model.WorkingTime;
import com.maksymchernenko.reserveit.repository.ReservationRepository;
import com.maksymchernenko.reserveit.repository.RestaurantTableRepository;
import com.maksymchernenko.reserveit.repository.WorkingTimeRepository;
import com.maksymchernenko.reserveit.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantTableRepository restaurantTableRepository;
    private final WorkingTimeRepository workingTimeRepository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  RestaurantTableRepository restaurantTableRepository,
                                  WorkingTimeRepository workingTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.restaurantTableRepository = restaurantTableRepository;
        this.workingTimeRepository = workingTimeRepository;
    }

    @Transactional
    @Override
    public List<Reservation> getActualByClient(User client) {
        finishPassedReservations();

        return reservationRepository.getByClientAndStatuses(client, List.of(Reservation.Status.PENDING, Reservation.Status.RESERVED));
    }

    @Transactional
    @Override
    public List<Reservation> getHistoryByClient(User client) {
        finishPassedReservations();

        return reservationRepository.getByClientAndStatuses(client, List.of(Reservation.Status.CANCELED, Reservation.Status.FINISHED));
    }

    @Transactional
    @Override
    public List<Reservation> getAll() {
        finishPassedReservations();

        return reservationRepository.getAll();
    }

    @Transactional
    @Override
    public List<Reservation> getAll(String filter, User manager) {
        finishPassedReservations();

        List<Reservation> reservations =  reservationRepository.getAll();
        reservations.removeIf(reservation -> reservation.getManager() != null && !reservation.getManager().equals(manager));
        switch (filter) {
            case "status" -> reservations.sort(Comparator.comparing(Reservation::getStatus));
            case "dateTime" -> reservations.sort(Comparator.comparing(Reservation::getDayTime));
            case "restaurant" -> reservations.sort(Comparator.comparing(r -> r.getTable().getRestaurant().getName()));
            default -> {}
        }

        return reservations;
    }

    private void finishPassedReservations() {
        List<Reservation> reservations = reservationRepository.getAll();
        for (Reservation reservation : reservations) {
            if (reservation.getStatus() == Reservation.Status.RESERVED
                    && LocalDateTime.now().isAfter(reservation.getDayTime().plusHours(RESERVATION_DURATION_OF_HOURS))) {

                reservation.setStatus(Reservation.Status.FINISHED);
                reservationRepository.update(reservation);
            }
        }
    }

    @Override
    public Map<RestaurantTable, List<LocalDateTime>> getAvailableTablesMap(long restaurantId, int numberOfGuests) {
        List<RestaurantTable> tables = restaurantTableRepository.getBySeatsNumber(restaurantId, numberOfGuests);
        List<WorkingTime> times = workingTimeRepository.getByDaysNumber(restaurantId, AVAILABLE_DAYS_FOR_RESERVATION);
        List<Reservation> allReservations = reservationRepository.getAll();

        Map<RestaurantTable, List<LocalDateTime>> map = new HashMap<>();
        for (RestaurantTable table : tables) {
            map.put(table, new ArrayList<>());
            for (WorkingTime workingTime : times) {
                List<LocalDateTime> dateTimes = new ArrayList<>();
                Map<LocalTime, Integer> generatedTimes = generateTimes(workingTime.getOpenTime(), workingTime.getCloseTime().minusHours(RESERVATION_DURATION_OF_HOURS));

                generatedTimes.forEach((time, day) -> {
                    LocalDate nearestDate = getNearestDate(day == 1 ? workingTime.getDayOfWeek() : workingTime.getDayOfWeek().plus(1));
                    if (!nearestDate.equals(LocalDate.now())
                        || nearestDate.equals(LocalDate.now())
                            && time.isAfter(LocalTime.now())) {
                        dateTimes.add(LocalDateTime.of(nearestDate, time));
                    }
                });

                map.get(table).addAll(dateTimes);
            }
        }

        for (Reservation reservation : allReservations) {
            if ((reservation.getStatus() == Reservation.Status.PENDING
                    || reservation.getStatus() == Reservation.Status.RESERVED)
                    && map.containsKey(reservation.getTable())) {
                List<LocalDateTime> availableTimes = map.get(reservation.getTable());
                removeDateTimes(availableTimes, reservation.getDayTime(), reservation.getDayTime().plusHours(RESERVATION_DURATION_OF_HOURS));
            }
        }

        return map;
    }

    @Override
    public Reservation getReservation(long id) {
        if (reservationRepository.get(id).isEmpty()) {
            throw new ReservationNotFoundException("Reservation with id " + id + " not found");
        } else {
            return reservationRepository.get(id).get();
        }
    }

    @Transactional
    @Override
    public boolean reserve(long restaurantId, LocalDateTime dateTime, int numberOfGuests, User client) {
        Reservation.Status status = Reservation.Status.PENDING;
        RestaurantTable restaurantTable = null;
        for (Map.Entry<RestaurantTable, List<LocalDateTime>> entry : getAvailableTablesMap(restaurantId, numberOfGuests).entrySet()) {
            if (entry.getValue().contains(dateTime)) {
                restaurantTable = entry.getKey();
            }
        }

        if (restaurantTable == null) {
            return false;
        }

        reservationRepository.reserve(restaurantTable, client, status, dateTime, numberOfGuests);

        return true;
    }

    @Transactional
    @Override
    public boolean updateReservation(Reservation reservation) {
        Reservation.Status status = Reservation.Status.PENDING;
        reservation.setStatus(status);

        RestaurantTable restaurantTable = null;
        for (Map.Entry<RestaurantTable, List<LocalDateTime>> entry : getAvailableTablesMap(reservation.getTable().getRestaurant().getId(),
                reservation.getGuestsNumber()).entrySet()) {
            if (entry.getValue().contains(reservation.getDayTime())) {
                restaurantTable = entry.getKey();
            }
        }

        if (restaurantTable == null) {
            return false;
        }

        reservationRepository.update(reservation);

        return true;
    }

    @Transactional
    @Override
    public void cancelReservation(long id) {
        reservationRepository.cancelReservation(id);
    }

    @Transactional
    @Override
    public void submitReservation(long id, User manager) {
        Reservation reservation = this.getReservation(id);
        reservation.setStatus(Reservation.Status.RESERVED);
        reservation.setManager(manager);
        reservationRepository.update(reservation);
    }

    private static Map<LocalTime, Integer> generateTimes(LocalTime start, LocalTime end) {
        Map<LocalTime, Integer> times = new HashMap<>();
        LocalTime time = start;

        do {
            if (time.isBefore(LocalTime.NOON.minusMinutes(1))
                    && time.isAfter(LocalTime.MIDNIGHT)
                    || time.equals(LocalTime.MIDNIGHT)) {
                times.put(time, 2);
            } else {
                times.put(time, 1);
            }

            time = time.plusMinutes(MINUTES_INTERVAL);
        } while (!time.equals(end.plusMinutes(MINUTES_INTERVAL)));

        return times;
    }

    private static LocalDate getNearestDate(DayOfWeek dayOfWeek) {
        DayOfWeek day = LocalDate.now().getDayOfWeek();
        LocalDate date = LocalDate.now();
        while (day != dayOfWeek) {
            day = day.plus(1);
            date = date.plusDays(1);
        }

        return date;
    }

    private static void removeDateTimes(List<LocalDateTime> times, LocalDateTime start, LocalDateTime end) {
        times.removeIf(time -> time.isBefore(end.plusMinutes(1)) && time.isAfter(start.minusMinutes(1)) || time.isBefore(LocalDateTime.now()));
    }
}
