package com.maksymchernenko.reserveit.controller;

import com.maksymchernenko.reserveit.exceptions.RestaurantAlreadyExistsException;
import com.maksymchernenko.reserveit.model.Restaurant;
import com.maksymchernenko.reserveit.model.RestaurantTable;
import com.maksymchernenko.reserveit.model.WorkingTime;
import com.maksymchernenko.reserveit.service.RestaurantService;
import com.maksymchernenko.reserveit.service.RestaurantTableService;
import com.maksymchernenko.reserveit.service.WorkingTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

@Controller
@RequestMapping("/manager/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final WorkingTimeService workingTimeService;
    private final RestaurantTableService restaurantTableService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService,
                                WorkingTimeService workingTimeService,
                                RestaurantTableService restaurantTableService) {
        this.restaurantService = restaurantService;
        this.workingTimeService = workingTimeService;
        this.restaurantTableService = restaurantTableService;
    }


    @GetMapping
    public String getAllRestaurantsPage(Model model) {
        model.addAttribute("restaurants", restaurantService.getAllRestaurants());

        return "manager/restaurants";
    }

    @GetMapping("/{id}")
    public String getRestaurantPage(Model model,
                                    @PathVariable long id) {
        Map<DayOfWeek, WorkingTime> workingTimeMap = workingTimeService.getWorkingTimeMap(id);

        model.addAttribute("workingTimeMap", workingTimeMap);
        model.addAttribute("restaurant", restaurantService.getRestaurant(id));
        model.addAttribute("tables", restaurantTableService.getTables(id));

        return "manager/restaurant";
    }

    @GetMapping("/create")
    public String getCreateRestaurantPage(Model model) {
        model.addAttribute("newRestaurant", new Restaurant());

        return "manager/create_restaurant";
    }

    @PostMapping("/create")
    public String createRestaurant(@ModelAttribute Restaurant restaurant, RedirectAttributes redirectAttributes) {
        try {
            Restaurant saved = restaurantService.createRestaurant(restaurant);

            return "redirect:/manager/restaurants/create/" + saved.getId();
        } catch (RestaurantAlreadyExistsException e) {
            redirectAttributes.addAttribute("error", "true");

            return "redirect:/manager/restaurants/create";
        }
    }

    @GetMapping("/create/{id}")
    public String getCreateTablesAndTimesPage(@PathVariable Long id,
                                              Model model) {
        Restaurant restaurant = restaurantService.getRestaurant(id);

        WorkingTime newWorkingTime = new WorkingTime();
        newWorkingTime.setRestaurant(restaurant);

        RestaurantTable newRestaurantTable = new RestaurantTable();
        newRestaurantTable.setRestaurant(restaurant);

        List<RestaurantTable> tables = restaurantTableService.getTables(id);
        Map<DayOfWeek, WorkingTime> workingTimeMap = workingTimeService.getWorkingTimeMap(id);

        model.addAttribute("workingTimeMap", workingTimeMap);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("newWorkingTime", newWorkingTime);
        model.addAttribute("newRestaurantTable", newRestaurantTable);
        model.addAttribute("tables", tables);
        model.addAttribute("availableTimes", generateTimes());
        model.addAttribute("tablesNumber", null);

        return "manager/create_tables_and_times";
    }

    @PostMapping("/create/{id}/addtime")
    public String addTimeToRestaurant(@PathVariable Long id,
                                      @ModelAttribute WorkingTime newWorkingTime,
                                      RedirectAttributes redirectAttributes) {
        addTime(id, newWorkingTime, redirectAttributes);

        return "redirect:/manager/restaurants/create/{id}";
    }

    @PostMapping("/create/{id}/addtable")
    public String addTableToRestaurant(@PathVariable Long id,
                                       @ModelAttribute RestaurantTable newRestaurantTable,
                                       @RequestParam Integer tablesNumber,
                                       RedirectAttributes redirectAttributes) {
        addTable(id, newRestaurantTable, tablesNumber, redirectAttributes);

        return "redirect:/manager/restaurants/create/{id}";
    }

    @PostMapping("/create/{id}/deletetime/{day}")
    public String deleteTimeForCreateRestaurant(@PathVariable long id,
                                                @PathVariable DayOfWeek day,
                                                RedirectAttributes redirectAttributes) {
        workingTimeService.delete(id, day);

        redirectAttributes.addAttribute("id", id);

        return "redirect:/manager/restaurants/create/{id}";
    }

    @PostMapping("/{restaurantId}/create/tables/{tableId}/delete")
    public String deleteTableForCreateRestaurant(@PathVariable long restaurantId,
                                                 @PathVariable long tableId,
                                                 RedirectAttributes redirectAttributes) {
        if (!restaurantTableService.delete(tableId)) {
            redirectAttributes.addFlashAttribute("deleted", false);
        }

        return "redirect:/manager/restaurants/create/{restaurantId}";
    }

    @GetMapping("/{id}/edit")
    public String getEditRestaurantPage(@PathVariable long id,
                                        Model model) {
        Map<DayOfWeek, WorkingTime> workingTimeMap = workingTimeService.getWorkingTimeMap(id);

        List<RestaurantTable> tables = restaurantTableService.getTables(id);

        Restaurant restaurant = restaurantService.getRestaurant(id);
        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setId(id);
        newRestaurant.setName(restaurant.getName());
        newRestaurant.setAddress(restaurant.getAddress());

        model.addAttribute("tables", tables);
        model.addAttribute("newRestaurant", newRestaurant);
        model.addAttribute("workingTimeMap", workingTimeMap);
        model.addAttribute("availableTimes", generateTimes());
        model.addAttribute("newWorkingTime", new WorkingTime(restaurant, null, null, null));
        model.addAttribute("newRestaurantTable", new RestaurantTable(restaurant, null));

        return "manager/edit_restaurant";
    }

    @PostMapping("/{id}/edit")
    public String editRestaurant(@ModelAttribute Restaurant newRestaurant,
                                 @PathVariable long id,
                                 RedirectAttributes redirectAttributes) {


        Restaurant restaurant = restaurantService.getRestaurant(id);
        restaurant.setName(newRestaurant.getName());
        restaurant.setAddress(newRestaurant.getAddress());
        restaurantService.updateRestaurant(restaurant);

        redirectAttributes.addAttribute("id", restaurant.getId());

        return "redirect:/manager/restaurants/{id}";
    }

    @PostMapping("/{id}/edit/addtime")
    public String editTimeForRestaurant(@PathVariable Long id,
                                        @ModelAttribute WorkingTime newWorkingTime,
                                        RedirectAttributes redirectAttributes) {
        addTime(id, newWorkingTime, redirectAttributes);

        return "redirect:/manager/restaurants/{id}/edit";
    }

    @PostMapping("/{id}/edit/addtable")
    public String editTableForRestaurant(@PathVariable Long id,
                                         @ModelAttribute RestaurantTable newRestaurantTable,
                                         @RequestParam(required=false) Integer tablesNumber,
                                         RedirectAttributes redirectAttributes) {
        addTable(id, newRestaurantTable, tablesNumber, redirectAttributes);

        return "redirect:/manager/restaurants/{id}/edit";
    }

    @PostMapping("/{restaurantId}/edit/tables/{tableId}/delete")
    public String deleteTableForEditRestaurant(@PathVariable long restaurantId,
                              @PathVariable long tableId,
                              RedirectAttributes redirectAttributes) {
        if (!restaurantTableService.delete(tableId)) {
            redirectAttributes.addFlashAttribute("deleted", false);
        }

        return "redirect:/manager/restaurants/{restaurantId}/edit";
    }

    @PostMapping("/{id}/edit/deletetime/{day}")
    public String deleteTimeForEditRestaurant(@PathVariable long id,
                                              @PathVariable DayOfWeek day,
                                              RedirectAttributes redirectAttributes) {
        workingTimeService.delete(id, day);

        redirectAttributes.addAttribute("id", id);

        return "redirect:/manager/restaurants/{id}/edit";
    }

    @PostMapping("/{id}/delete")
    public String deleteRestaurant(@PathVariable long id) {
        restaurantService.deleteRestaurant(id);

        return "redirect:/manager/restaurants";
    }

    private void addTime(@PathVariable Long id,
                         @ModelAttribute WorkingTime newWorkingTime,
                         RedirectAttributes redirectAttributes) {
        Restaurant restaurant = restaurantService.getRestaurant(id);
        newWorkingTime.setRestaurant(restaurant);
        workingTimeService.createWorkingTime(new WorkingTime(restaurant, newWorkingTime.getDayOfWeek(), newWorkingTime.getOpenTime(), newWorkingTime.getCloseTime()));

        redirectAttributes.addAttribute("id", newWorkingTime.getRestaurant().getId());
    }

    private void addTable(@PathVariable Long id,
                          @ModelAttribute RestaurantTable newRestaurantTable,
                          @RequestParam(required = false) Integer tablesNumber,
                          RedirectAttributes redirectAttributes) {
        Restaurant restaurant = restaurantService.getRestaurant(id);
        newRestaurantTable.setRestaurant(restaurant);
        for (int i = 0; i < tablesNumber; i++) {
            restaurantTableService.createTable(new RestaurantTable(newRestaurantTable.getRestaurant(), newRestaurantTable.getSeatsNumber()));
        }

        redirectAttributes.addAttribute("id", newRestaurantTable.getRestaurant().getId());
    }

    private List<LocalTime> generateTimes() {
        List<LocalTime> times = new ArrayList<>();
        for (int h = 0; h < 24; h++) {
            for (int m = 0; m < 60; m += 15) {
                times.add(LocalTime.of(h, m));
            }
        }

        return times;
    }
}
