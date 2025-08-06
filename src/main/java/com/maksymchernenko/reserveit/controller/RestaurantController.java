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
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

@Controller
@RequestMapping()
@SessionAttributes({"workingTimeMap", "tablesMap"})
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

    @ModelAttribute("workingTimeMap")
    public Map<DayOfWeek, WorkingTime> workingTimeMap() {
        return new HashMap<>();
    }

    @ModelAttribute("tablesMap")
    public Map<Integer, Integer> tablesMap() {
        return new HashMap<>();
    }

    @GetMapping("/manager/restaurants")
    public String getAllRestaurantsPage(Model model) {
        model.addAttribute("restaurants", restaurantService.getAllRestaurants());

        return "manager/restaurants";
    }

    @GetMapping("/manager/restaurant/{id}/more")
    public String getRestaurantPage(Model model,
                                    SessionStatus sessionStatus,
                                    @PathVariable long id) {
        sessionStatus.setComplete();

        model.addAttribute("restaurant", restaurantService.getRestaurant(id));
        model.addAttribute("workingTimeMap", workingTimeService.getWorkingTimeMap(id));
        model.addAttribute("tableMap", restaurantTableService.getTableMap(id));

        return "manager/restaurant";
    }

    @GetMapping("/manager/restaurant/create")
    public String getCreateRestaurantPage(Model model) {
        model.addAttribute("newRestaurant", new Restaurant());

        return "manager/create_restaurant";
    }

    @PostMapping("/manager/restaurant/create")
    public String createRestaurant(@ModelAttribute Restaurant restaurant, RedirectAttributes redirectAttributes) {
        try {
            Restaurant saved = restaurantService.createRestaurant(restaurant);
            redirectAttributes.addAttribute("id", saved.getId());

            return "redirect:/manager/restaurant/tablesandtimes/create";
        } catch (RestaurantAlreadyExistsException e) {
            redirectAttributes.addAttribute("error", "true");

            return "redirect:/manager/restaurant/create";
        }
    }

    @GetMapping("/manager/restaurant/tablesandtimes/create")
    public String getCreateTablesAndTimesPage(@RequestParam long id,
                                              @ModelAttribute("workingTimeMap") Map<DayOfWeek, WorkingTime> workingTimeMap,
                                              @ModelAttribute("tablesMap") Map<Integer, Integer> tablesMap,
                                              Model model) {
        WorkingTime newWorkingTime = new WorkingTime();
        newWorkingTime.setRestaurant(restaurantService.getRestaurant(id));

        RestaurantTable newRestaurantTable = new RestaurantTable();
        newRestaurantTable.setRestaurant(restaurantService.getRestaurant(id));

        model.addAttribute("restaurant", restaurantService.getRestaurant(id));
        model.addAttribute("availableTimes", generateTimes());
        model.addAttribute("newWorkingTime", newWorkingTime);
        model.addAttribute("newRestaurantTable", newRestaurantTable);
        model.addAttribute("tablesNumber", null);

        return "manager/create_tables_and_times";
    }

    @PostMapping("/manager/restaurant/tablesandtimes/create")
    public String createTablesAndTimes(@RequestParam("id") long id,
                                       @ModelAttribute("workingTimeMap") Map<DayOfWeek, WorkingTime> workingTimeMap,
                                       @ModelAttribute("tablesMap") Map<Integer, Integer> tablesMap,
                                       @ModelAttribute WorkingTime newWorkingTime,
                                       SessionStatus sessionStatus) {
        Restaurant restaurant = restaurantService.getRestaurant(id);
        newWorkingTime.setRestaurant(restaurant);

        for (WorkingTime workingTime : workingTimeMap.values()) {
            workingTimeService.createWorkingTime(workingTime);
        }

        for (Map.Entry<Integer, Integer> entry : tablesMap.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                restaurantTableService.createTable(new RestaurantTable(restaurant, entry.getKey()));
            }
        }

        sessionStatus.setComplete();

        return "redirect:/manager/restaurants";
    }

    @PostMapping("/manager/restaurant/create/addttime")
    public String addTimeToRestaurant(@ModelAttribute WorkingTime newWorkingTime,
                                      @ModelAttribute("workingTimeMap") Map<DayOfWeek, WorkingTime> workingTimeMap,
                                      RedirectAttributes redirectAttributes) {
        workingTimeMap.put(newWorkingTime.getDayOfWeek(), newWorkingTime);

        redirectAttributes.addAttribute("id", newWorkingTime.getRestaurant().getId());

        return "redirect:/manager/restaurant/tablesandtimes/create";
    }

    @PostMapping("/manager/restaurant/create/addtable")
    public String addTableToRestaurant(@ModelAttribute RestaurantTable newRestaurantTable,
                                       @RequestParam(required=false) Integer tablesNumber,
                                       @ModelAttribute("tablesMap") Map<Integer, Integer> tablesMap,
                                       RedirectAttributes redirectAttributes) {
        if (tablesMap.containsKey(newRestaurantTable.getSeatsNumber())) {
            tablesMap.replace(newRestaurantTable.getSeatsNumber(), tablesMap.get(newRestaurantTable.getSeatsNumber()) + tablesNumber);
        } else {
            tablesMap.put(newRestaurantTable.getSeatsNumber(), tablesNumber);
        }

        redirectAttributes.addAttribute("id", newRestaurantTable.getRestaurant().getId());

        return "redirect:/manager/restaurant/tablesandtimes/create";
    }

    @PostMapping("/manager/restaurant/{id}/create/deletetime/{day}")
    public String deleteTimeForCreateRestaurant(@PathVariable long id,
                                                @PathVariable DayOfWeek day,
                                                @ModelAttribute("workingTimeMap") Map<DayOfWeek, WorkingTime> workingTimeMap,
                                                RedirectAttributes redirectAttributes) {
        workingTimeMap.remove(day);

        redirectAttributes.addAttribute("id", id);

        return "redirect:/manager/restaurant/tablesandtimes/create";
    }

    @GetMapping("/manager/restaurant/{id}/edit")
    public String getEditRestaurantPage(@PathVariable long id,
                                        Model model,
                                        @ModelAttribute("workingTimeMap") Map<DayOfWeek, WorkingTime> workingTimeMap,
                                        @ModelAttribute("tablesMap") Map<Integer, Integer> tablesMap) {
        if (workingTimeMap.isEmpty()) {
            workingTimeMap.putAll(workingTimeService.getWorkingTimeMap(id));
        }

        if (tablesMap.isEmpty()) {
            tablesMap.putAll(restaurantTableService.getTableMap(id));
        }

        Restaurant restaurant = restaurantService.getRestaurant(id);
        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setId(id);
        newRestaurant.setName(restaurant.getName());
        newRestaurant.setAddress(restaurant.getAddress());

        model.addAttribute("newRestaurant", newRestaurant);
        model.addAttribute("workingTimeMap", workingTimeMap);
        model.addAttribute("tablesMap", tablesMap);
        model.addAttribute("availableTimes", generateTimes());
        model.addAttribute("newWorkingTime", new WorkingTime(restaurant, null, null, null));
        model.addAttribute("newRestaurantTable", new RestaurantTable(restaurant, null));

        return "manager/edit_restaurant";
    }

    @PostMapping("/manager/restaurant/{id}/edit")
    public String editRestaurant(@ModelAttribute Restaurant newRestaurant,
                                 @PathVariable long id,
                                 @ModelAttribute("workingTimeMap") Map<DayOfWeek, WorkingTime> workingTimeMap,
                                 @ModelAttribute("tablesMap") Map<Integer, Integer> tablesMap,
                                 SessionStatus sessionStatus,
                                 RedirectAttributes redirectAttributes) {
        Restaurant restaurant = restaurantService.getRestaurant(id);
        restaurant.setName(newRestaurant.getName());
        restaurant.setAddress(newRestaurant.getAddress());
        restaurant.setNumberOfTables(0);
        restaurantService.updateRestaurant(restaurant);

        workingTimeService.deleteAll(id);
        for (WorkingTime workingTime : workingTimeMap.values()) {
            workingTimeService.createWorkingTime(workingTime);
        }

        restaurantTableService.deleteAll(id);
        for (Map.Entry<Integer, Integer> entry : tablesMap.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                restaurantTableService.createTable(new RestaurantTable(restaurant, entry.getKey()));
            }
        }

        sessionStatus.setComplete();

        redirectAttributes.addAttribute("id", restaurant.getId());

        return "redirect:/manager/restaurant/{id}/more";
    }

    @PostMapping("/manager/restaurant/edittime")
    public String editTimeForRestaurant(@ModelAttribute WorkingTime newWorkingTime,
                                        @ModelAttribute("workingTimeMap") Map<DayOfWeek, WorkingTime> workingTimeMap,
                                        RedirectAttributes redirectAttributes) {
        workingTimeMap.put(newWorkingTime.getDayOfWeek(), newWorkingTime);

        redirectAttributes.addAttribute("id", newWorkingTime.getRestaurant().getId());

        return "redirect:/manager/restaurant/{id}/edit";
    }

    @PostMapping("/manager/restaurant/edittable")
    public String editTableForRestaurant(@ModelAttribute RestaurantTable newRestaurantTable,
                                         @RequestParam(required=false) Integer tablesNumber,
                                         @ModelAttribute("tablesMap") Map<Integer, Integer> tablesMap,
                                         RedirectAttributes redirectAttributes) {
        if (tablesMap.containsKey(newRestaurantTable.getSeatsNumber())) {
            int newValue = tablesMap.get(newRestaurantTable.getSeatsNumber()) + tablesNumber;
            tablesMap.replace(newRestaurantTable.getSeatsNumber(), Math.max(newValue, 0));
        } else {
            tablesMap.put(newRestaurantTable.getSeatsNumber(), tablesNumber);
        }

        redirectAttributes.addAttribute("id", newRestaurantTable.getRestaurant().getId());

        return "redirect:/manager/restaurant/{id}/edit";
    }

    @PostMapping("/manager/restaurant/{id}/edit/deletetime/{day}")
    public String deleteTimeForEditRestaurant(@PathVariable long id,
                                          @PathVariable DayOfWeek day,
                                          @ModelAttribute("workingTimeMap") Map<DayOfWeek, WorkingTime> workingTimeMap,
                                          RedirectAttributes redirectAttributes) {
        workingTimeMap.remove(day);
        workingTimeService.delete(id, day);

        redirectAttributes.addAttribute("id", id);

        return "redirect:/manager/restaurant/{id}/edit";
    }

    @PostMapping("/manager/restaurant/{id}/delete")
    public String deleteRestaurant(@PathVariable long id) {
        restaurantService.deleteRestaurant(id);

        return "redirect:/manager/restaurants";
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
