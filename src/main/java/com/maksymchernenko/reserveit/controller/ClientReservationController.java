package com.maksymchernenko.reserveit.controller;

import com.maksymchernenko.reserveit.exceptions.UserNotFoundException;
import com.maksymchernenko.reserveit.model.Reservation;
import com.maksymchernenko.reserveit.model.RestaurantTable;
import com.maksymchernenko.reserveit.model.User;
import com.maksymchernenko.reserveit.service.ReservationService;
import com.maksymchernenko.reserveit.service.RestaurantService;
import com.maksymchernenko.reserveit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/client/reservations")
public class ClientReservationController {

    private final UserService userService;
    private final ReservationService reservationService;
    private final RestaurantService restaurantService;

    @Autowired
    public ClientReservationController(UserService userService,
                                       ReservationService reservationService,
                                       RestaurantService restaurantService) {
        this.userService = userService;
        this.reservationService = reservationService;
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public String getReservationsPage(Model model,
                                      Authentication authentication) {
        try {
            User client = userService.getByEmail(authentication.getName());

            model.addAttribute("reservations", reservationService.getByClient(client));

            return "client/reservations";
        } catch (UserNotFoundException e) {
            return "redirect:/user/logout";
        }
    }

    @GetMapping("/create/submitrestaurant")
    public String getSubmitRestaurantPage(Model model) {
        model.addAttribute("restaurants", restaurantService.getAllRestaurants());
        model.addAttribute("availableTimes", null);

        return "client/create_reservation";
    }

    @PostMapping("/create/submitrestaurant")
    public String submitRestaurant(@RequestParam Long restaurantId,
                                   @RequestParam Integer numberOfGuests,
                                   Model model) {
        Map<RestaurantTable, List<LocalDateTime>> availableTables = reservationService.getAvailableTablesMap(restaurantId, numberOfGuests);
        List<LocalDateTime> availableTimes = new ArrayList<>();

        fillAndSortTimes(availableTables, availableTimes);

        model.addAttribute("availableTimes", availableTimes);
        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("numberOfGuests", numberOfGuests);

        return "client/create_reservation";
    }

    @PostMapping("/create")
    public String createReservation(@RequestParam LocalDateTime dateTime,
                                    @RequestParam Integer numberOfGuests,
                                    @RequestParam Long restaurantId,
                                    Authentication authentication) {
        try {
            User client = userService.getByEmail(authentication.getName());
            reservationService.reserve(restaurantId, dateTime, numberOfGuests, client);

            return "redirect:/client/reservations";
        } catch (UserNotFoundException e) {
            return "redirect:/user/logout";
        }
    }

    @GetMapping("/{id}/edit")
    public String getEditReservationPage(Model model,
                                         @PathVariable Long id) {
        model.addAttribute("reservation", reservationService.getReservation(id));

        return "client/edit_reservation";
    }

    @PostMapping("/{id}/edit/guestsnumber")
    public String editNumberOfGuests(@PathVariable Long id,
                                     @RequestParam Integer numberOfGuests,
                                     Model model) {
        Reservation reservation = reservationService.getReservation(id);

        Map<RestaurantTable, List<LocalDateTime>> availableTables =
                reservationService.getAvailableTablesMap(reservation.getTable().getRestaurant().getId(), numberOfGuests);

        List<LocalDateTime> availableTimes = new ArrayList<>();

        if (reservation.getTable().getSeatsNumber() >= numberOfGuests) {
            for (int i = 0; i <= ReservationService.RESERVATION_DURATION_OF_HOURS * 60; i += 15) {
                availableTimes.add(reservation.getDayTime().plusMinutes(i));
            }
        }

        fillAndSortTimes(availableTables, availableTimes);

        model.addAttribute("availableTimes", availableTimes);
        model.addAttribute("numberOfGuests", numberOfGuests);
        model.addAttribute("reservation", reservation);

        return "client/edit_reservation";
    }

    @PostMapping("/{id}/edit")
    public String editReservation(@PathVariable Long id,
                                  @RequestParam LocalDateTime dateTime,
                                  @RequestParam Integer numberOfGuests) {
        Reservation reservation = reservationService.getReservation(id);
        reservation.setGuestsNumber(numberOfGuests);
        reservation.setDayTime(dateTime);

        reservationService.updateReservation(reservation);

        return "redirect:/client/reservations";
    }

    @PostMapping("/{id}/cancel")
    public String cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);

        return "redirect:/client/reservations";
    }

    private static void fillAndSortTimes(Map<RestaurantTable, List<LocalDateTime>> tablesMap, List<LocalDateTime> targetList) {
        for (List<LocalDateTime> list : tablesMap.values()) {
            for (LocalDateTime time : list) {
                if (!targetList.contains(time)) {
                    targetList.add(time);
                }
            }
        }

        targetList.sort(Comparator.naturalOrder());
    }
}
