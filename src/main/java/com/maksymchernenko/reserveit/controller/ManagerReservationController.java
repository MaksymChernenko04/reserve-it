package com.maksymchernenko.reserveit.controller;

import com.maksymchernenko.reserveit.exceptions.UserNotFoundException;
import com.maksymchernenko.reserveit.model.User;
import com.maksymchernenko.reserveit.service.ReservationService;
import com.maksymchernenko.reserveit.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling reservation-related actions
 * to view, filter, submit and cancel reservations.
 * <p>
 * Provides endpoints for managers.
 */
@Controller
@RequestMapping("/manager/reservations")
public class ManagerReservationController {

    private static final Logger logger = LoggerFactory.getLogger(ManagerReservationController.class);

    private final ReservationService reservationService;
    private final UserService userService;

    /**
     * Instantiates a new Manager reservation controller.
     *
     * @param reservationService the {@link ReservationService}
     * @param userService        the {@link UserService}
     */
    @Autowired
    public ManagerReservationController(ReservationService reservationService,
                                        UserService userService) {
        this.reservationService = reservationService;
        this.userService = userService;
    }

    /**
     * Displays all reservations page.
     *
     * @param model          the model to bind attributes for the view
     * @param authentication the authentication instance containing the current user
     * @return the list of all reservations page view name on success,
     * redirect to logout if the user is not found otherwise
     */
    @GetMapping
    public String getReservationsPage(Model model,
                                      Authentication authentication) {
        logger.info("GET /manager/reservations called");

        try {
            User manager = userService.getByEmail(authentication.getName());
            model.addAttribute("reservations", reservationService.getAll("default", manager));

            logger.info("All reservations page rendered for manager");

            return "manager/reservations";
        } catch (UserNotFoundException e) {
            logger.warn("Getting reservations failed. User with email = {} does not exist. " +
                    "Redirecting to logout", authentication.getName());

            return "redirect:/user/logout";
        }
    }

    /**
     * Filters reservations by one of the attributes.
     *
     * @param model           the model to bind attributes for the view
     * @param authentication  the authentication instance containing the current user
     * @param filterAttribute the filter attribute
     * @return the list of all sorted reservations page view name on success,
     * redirect to logout if the user is not found otherwise
     */
    @PostMapping("/filter")
    public String filterReservations(Model model,
                                     Authentication authentication,
                                     @RequestParam String filterAttribute) {
        logger.info("POST /manager/reservations/filter called");

        try {
            User manager = userService.getByEmail(authentication.getName());
            model.addAttribute("reservations", reservationService.getAll(filterAttribute, manager));

            logger.info("All reservations page rendered with filter for manager");

            return "manager/reservations";
        } catch (UserNotFoundException e) {
            logger.warn("Getting reservations with filter failed. User with email = {} does not exist. " +
                    "Redirecting to logout", authentication.getName());

            return "redirect:/user/logout";
        }
    }

    /**
     * Submits the resevation.
     *
     * @param id             the resevation id
     * @param authentication the authentication instance containing the current user
     * @return the list of all reservations page view name on success,
     * redirect to logout if the user is not found otherwise
     */
    @PostMapping("/{id}/submit")
    public String submitResevation(@PathVariable int id,
                                   Authentication authentication) {
        logger.info("POST /manager/reservations/{id}/submit called");

        try {
            User manager = userService.getByEmail(authentication.getName());
            reservationService.submitReservation(id, manager);

            logger.info("Redirecting to all reservations page after submit");

            return "redirect:/manager/reservations";
        } catch (UserNotFoundException e) {
            logger.warn("Getting reservations after submit failed. User with email = {} does not exist. " +
                    "Redirecting to logout", authentication.getName());

            return "redirect:/user/logout";
        }
    }

    /**
     * Cancels the resevation.
     *
     * @param id the resevation id
     * @return the list of all reservations page view name
     */
    @PostMapping("/{id}/cancel")
    public String cancelResevation(@PathVariable int id) {
        logger.info("POST /manager/reservations/{id}/cancel called");

        reservationService.cancelReservation(id);

        logger.info("Redirecting to all reservations page after cancel");

        return "redirect:/manager/reservations";
    }
}
