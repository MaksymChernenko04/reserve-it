package com.maksymchernenko.reserveit.controller;

import com.maksymchernenko.reserveit.exceptions.UserNotFoundException;
import com.maksymchernenko.reserveit.model.User;
import com.maksymchernenko.reserveit.service.ReservationService;
import com.maksymchernenko.reserveit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manager/reservations")
public class ManagerReservationController {

    private final ReservationService reservationService;
    private final UserService userService;

    @Autowired
    public ManagerReservationController(ReservationService reservationService,
                                        UserService userService) {
        this.reservationService = reservationService;
        this.userService = userService;
    }

    @GetMapping
    public String getReservationsPage(Model model,
                                      Authentication authentication) {
        try {
            User manager = userService.getByEmail(authentication.getName());
            model.addAttribute("reservations", reservationService.getAll("default", manager));

            return "manager/reservations";
        } catch (UserNotFoundException e) {
            return "redirect:/user/logout";
        }
    }

    @PostMapping("/filter")
    public String filterReservations(Model model,
                                     Authentication authentication,
                                     @RequestParam String filterAttribute) {
        try {
            User manager = userService.getByEmail(authentication.getName());
            model.addAttribute("reservations", reservationService.getAll(filterAttribute, manager));

            return "manager/reservations";
        } catch (UserNotFoundException e) {
            return "redirect:/user/logout";
        }
    }

    @PostMapping("/{id}/submit")
    public String submitResevation(@PathVariable int id,
                                   Authentication authentication) {
        try {
            User manager = userService.getByEmail(authentication.getName());
            reservationService.submitReservation(id, manager);

            return "redirect:/manager/reservations";
        } catch (UserNotFoundException e) {
            return "redirect:/user/logout";
        }
    }

    @PostMapping("/{id}/cancel")
    public String cancelResevation(@PathVariable int id) {
        reservationService.cancelReservation(id);

        return "redirect:/manager/reservations";
    }
}
