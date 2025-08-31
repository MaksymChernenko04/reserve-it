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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Controller responsible for handling reservation-related actions to view actual reservations
 * as well as the reservation history, create, edit and cancel reservations.
 * <p>
 * Provides endpoints for clients.
 */
@Controller
@RequestMapping("/client/reservations")
public class ClientReservationController {

    private final UserService userService;
    private final ReservationService reservationService;
    private final RestaurantService restaurantService;

    /**
     * Instantiates a new Client reservation controller.
     *
     * @param userService        the {@link UserService}
     * @param reservationService the {@link ReservationService}
     * @param restaurantService  the {@link RestaurantService}
     */
    @Autowired
    public ClientReservationController(UserService userService,
                                       ReservationService reservationService,
                                       RestaurantService restaurantService) {
        this.userService = userService;
        this.reservationService = reservationService;
        this.restaurantService = restaurantService;
    }

    /**
     * Displays actual user reservations page.
     *
     * @param model          the model to bind attributes for the view
     * @param authentication the authentication instance containing the current user
     * @return the list of actual user reservations page view name on success,
     * redirect to logout if the user is not found otherwise
     */
    @GetMapping
    public String getReservationsPage(Model model,
                                      Authentication authentication) {
        try {
            User client = userService.getByEmail(authentication.getName());

            model.addAttribute("reservations", reservationService.getActualByClient(client));

            return "client/reservations";
        } catch (UserNotFoundException e) {
            return "redirect:/user/logout";
        }
    }

    /**
     * Displays a submit restaurant page.
     *
     * @param model the model to bind attributes for the view
     * @return the submit restaurant page view name
     */
    @GetMapping("/create/submitrestaurant")
    public String getSubmitRestaurantPage(Model model) {
        model.addAttribute("restaurants", restaurantService.getAllRestaurants());
        model.addAttribute("availableTimes", null);

        return "client/create_reservation";
    }

    /**
     * Submits the choice of the restaurant for a reservation.
     *
     * @param restaurantId   the restaurant id
     * @param numberOfGuests the number of guests
     * @param model          the model to bind attributes for the view
     * @return the reservation creation page view name
     */
    @PostMapping("/create/submitrestaurant")
    public String submitRestaurant(@RequestParam Long restaurantId,
                                   @RequestParam Integer numberOfGuests,
                                   Model model) {
        Map<RestaurantTable, List<LocalDateTime>> availableTables =
                reservationService.getAvailableTablesMap(restaurantId, numberOfGuests);
        List<LocalDateTime> availableTimes = new ArrayList<>();

        fillAndSortTimes(availableTables, availableTimes);

        model.addAttribute("availableTimes", availableTimes);
        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("numberOfGuests", numberOfGuests);

        return "client/create_reservation";
    }

    /**
     * Creates a new reservation.
     *
     * @param dateTime           the date and time
     * @param numberOfGuests     the number of guests
     * @param restaurantId       the restaurant id
     * @param authentication     the authentication instance containing the current user
     * @param redirectAttributes the redirect attributes used to pass a message either the reservation is created on redirect
     * @return redirect to the list of actual user reservations on success,
     * redirect to logout if the user is not found otherwise
     */
    @PostMapping("/create")
    public String createReservation(@RequestParam LocalDateTime dateTime,
                                    @RequestParam Integer numberOfGuests,
                                    @RequestParam Long restaurantId,
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes) {
        try {
            User client = userService.getByEmail(authentication.getName());

            if (!reservationService.reserve(restaurantId, dateTime, numberOfGuests, client)) {
                redirectAttributes.addFlashAttribute("message", "Creating failed");
            }

            return "redirect:/client/reservations";
        } catch (UserNotFoundException e) {
            return "redirect:/user/logout";
        }
    }

    /**
     * Displays an edit reservation page.
     *
     * @param model the model to bind attributes for the view
     * @param id    the reservation id
     * @return the edit reservation page view name
     */
    @GetMapping("/{id}/edit")
    public String getEditReservationPage(Model model,
                                         @PathVariable Long id) {
        model.addAttribute("reservation", reservationService.getReservation(id));

        return "client/edit_reservation";
    }

    /**
     * Updates the number of guests in the reservation.
     *
     * @param id             the reservation id
     * @param numberOfGuests the new number of guests
     * @param model          the model to bind attributes for the view
     * @return the edit reservation page view name
     */
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

    /**
     * Updates the reservation.
     *
     * @param id                 the reservation id
     * @param dateTime           the new date and time
     * @param numberOfGuests     the new number of guests
     * @param redirectAttributes the redirect attributes used to pass a message either the reservation is updated on redirect
     * @return redirect to the list of actual user reservations
     */
    @PostMapping("/{id}/edit")
    public String editReservation(@PathVariable Long id,
                                  @RequestParam LocalDateTime dateTime,
                                  @RequestParam Integer numberOfGuests,
                                  RedirectAttributes redirectAttributes) {
        Reservation reservation = reservationService.getReservation(id);
        reservation.setGuestsNumber(numberOfGuests);
        reservation.setDayTime(dateTime);

        if (!reservationService.updateReservation(reservation)) {
            redirectAttributes.addFlashAttribute("message", "Updating failed");
        }

        return "redirect:/client/reservations";
    }

    /**
     * Cancels the reservation.
     *
     * @param id the reservation id
     * @return redirect to the list of actual user reservations
     */
    @PostMapping("/{id}/cancel")
    public String cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);

        return "redirect:/client/reservations";
    }

    /**
     * Displays reservation history page.
     *
     * @param model          the model to bind attributes for the view
     * @param authentication the authentication instance containing the current user
     * @return the reservation history page on success,
     * redirect to logout if the user is not found otherwise
     */
    @GetMapping("/history")
    public String getReservationHistoryPage(Model model,
                                            Authentication authentication) {
        try {
            User client = userService.getByEmail(authentication.getName());
            model.addAttribute("reservations", reservationService.getHistoryByClient(client));

            return "client/reservations_history";
        } catch (UserNotFoundException e) {
            return "redirect:/user/logout";
        }
    }

    private static void fillAndSortTimes(Map<RestaurantTable,
                                         List<LocalDateTime>> tablesMap,
                                         List<LocalDateTime> targetList) {
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
