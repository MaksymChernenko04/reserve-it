package com.maksymchernenko.reserveit.controller;

import com.maksymchernenko.reserveit.exceptions.RestaurantAlreadyExistsException;
import com.maksymchernenko.reserveit.model.Restaurant;
import com.maksymchernenko.reserveit.model.WorkingTime;
import com.maksymchernenko.reserveit.service.RestaurantService;
import com.maksymchernenko.reserveit.service.WorkingTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping()
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final WorkingTimeService workingTimeService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService, WorkingTimeService workingTimeService) {
        this.restaurantService = restaurantService;
        this.workingTimeService = workingTimeService;
    }

    @GetMapping("/manager/restaurants")
    public String getAllRestaurantsPage(Model model) {
        model.addAttribute("restaurants", restaurantService.getAllRestaurants());

        return "manager/restaurants";
    }

    @GetMapping("/manager/restaurant/{id}/more")
    public String getRestaurantPage(Model model, @PathVariable long id) {
        model.addAttribute("restaurant", restaurantService.getRestaurant(id));
        model.addAttribute("workingTime", restaurantService.getWorkingTime(id));

        return "manager/restaurant";
    }

    @GetMapping("/manager/restaurant/create")
    public String getCreateRestaurantPage(Model model) {
        model.addAttribute("restaurant", new Restaurant());

        return "manager/create_restaurant";
    }

    @PostMapping("/manager/restaurant/create")
    public String createRestaurant(@ModelAttribute Restaurant restaurant, RedirectAttributes redirectAttributes) {
        try {
            Restaurant saved = restaurantService.createRestaurant(restaurant);
            redirectAttributes.addAttribute("id", saved.getId());

            return "redirect:/manager/restaurant/create/addtablesandtimes";
        } catch (RestaurantAlreadyExistsException e) {
            redirectAttributes.addAttribute("error", "true");

            return "redirect:/manager/restaurant/create";
        }
    }

    @GetMapping("/manager/restaurant/create/addtablesandtimes")
    public String getCreateTablesAndTimesPage(@RequestParam long id, Model model) {
        WorkingTime workingTime = new WorkingTime();
        workingTime.setRestaurant(restaurantService.getRestaurant(id));

        List<LocalTime> availableTimes = new ArrayList<>();
        for (int h = 0; h < 24; h++) {
            for (int m = 0; m < 60; m += 15) {
                availableTimes.add(LocalTime.of(h, m));
            }
        }

        model.addAttribute("availableTimes", availableTimes);
        model.addAttribute("workingTime", workingTime);

        return  "manager/create_tables_and_times";
    }

    @PostMapping("/manager/restaurant/create/addtablesandtimes")
    public String createTablesAndTimes(@ModelAttribute WorkingTime workingTime, RedirectAttributes redirectAttributes) {
        Restaurant restaurant = restaurantService.getRestaurant(workingTime.getRestaurant().getId());
        workingTime.setRestaurant(restaurant);

        workingTimeService.createWorkingTime(workingTime);

        redirectAttributes.addAttribute("id", restaurant.getId());

        return "redirect:/manager/restaurant/create/addtablesandtimes";
    }

    @PostMapping("/manager/restaurant/{id}/delete")
    public String deleteRestaurant(@PathVariable long id) {
        restaurantService.deleteRestaurant(id);

        return "redirect:/manager/restaurants";
    }
}
