package com.maksymchernenko.reserveit.controller;

import com.maksymchernenko.reserveit.exceptions.RestaurantAlreadyExistsException;
import com.maksymchernenko.reserveit.model.Restaurant;
import com.maksymchernenko.reserveit.model.RestaurantTable;
import com.maksymchernenko.reserveit.model.WorkingTime;
import com.maksymchernenko.reserveit.service.RestaurantService;
import com.maksymchernenko.reserveit.service.RestaurantTableService;
import com.maksymchernenko.reserveit.service.WorkingTimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

/**
 * Controller responsible for handling restaurant-related actions such as
 * restaurant, restaurant table and working time CRUD methods.
 * <p>
 * Provides endpoints for managers.
 */
@Controller
@RequestMapping("/manager/restaurants")
public class RestaurantController {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantController.class);

    private final RestaurantService restaurantService;
    private final WorkingTimeService workingTimeService;
    private final RestaurantTableService restaurantTableService;

    /**
     * Instantiates a new Restaurant controller.
     *
     * @param restaurantService      the {@link RestaurantService}
     * @param workingTimeService     the {@link WorkingTimeService}
     * @param restaurantTableService the {@link RestaurantTableService}
     */
    @Autowired
    public RestaurantController(RestaurantService restaurantService,
                                WorkingTimeService workingTimeService,
                                RestaurantTableService restaurantTableService) {
        this.restaurantService = restaurantService;
        this.workingTimeService = workingTimeService;
        this.restaurantTableService = restaurantTableService;
    }

    /**
     * Displays all restaurants page.
     *
     * @param model the model to bind attributes for the view
     * @return the list of all restaurants page view name
     */
    @GetMapping
    public String getAllRestaurantsPage(Model model) {
        logger.info("GET /manager/restaurants called");

        model.addAttribute("restaurants", restaurantService.getAllRestaurants());

        logger.info("All restaurants page rendered");

        return "manager/restaurants";
    }

    /**
     * Displays a restaurant page with the list of tables and working times.
     *
     * @param model the model to bind attributes for the view
     * @param id    the restaurant id
     * @return the restaurant page view name
     */
    @GetMapping("/{id}")
    public String getRestaurantPage(Model model,
                                    @PathVariable long id) {
        logger.info("GET /manager/restaurants/{id} called");

        Map<DayOfWeek, WorkingTime> workingTimeMap = workingTimeService.getWorkingTimeMap(id);

        model.addAttribute("workingTimeMap", workingTimeMap);
        model.addAttribute("restaurant", restaurantService.getRestaurant(id));
        model.addAttribute("tables", restaurantTableService.getTables(id));

        logger.info("Restaurant page rendered");

        return "manager/restaurant";
    }

    /**
     * Displays a restaurant creation page.
     *
     * @param model the model to bind attributes for the view
     * @return the restaurant creation page view name
     */
    @GetMapping("/create")
    public String getCreateRestaurantPage(Model model) {
        logger.info("GET /manager/restaurants/create called");

        model.addAttribute("newRestaurant", new Restaurant());

        logger.info("Restaurant creation page rendered");

        return "manager/create_restaurant";
    }

    /**
     * Creates a new restaurant.
     *
     * @param restaurant         the restaurant to create
     * @param redirectAttributes the redirect attributes used to pass error flags on redirect
     * @return redirect to the tables and working times creation page on success,
     * redirect to the creation page if the restaurant is already exists otherwise
     */
    @PostMapping("/create")
    public String createRestaurant(@ModelAttribute Restaurant restaurant,
                                   RedirectAttributes redirectAttributes) {
        logger.info("POST /manager/restaurants/create called");

        try {
            Restaurant saved = restaurantService.createRestaurant(restaurant);

            logger.info("Redirecting to updated creation page");

            return "redirect:/manager/restaurants/create/" + saved.getId();
        } catch (RestaurantAlreadyExistsException e) {
            redirectAttributes.addAttribute("error", "true");

            logger.warn("Restaurant creation failed. Restaurant with name = {} already exists. " +
                    "Redirecting to restaurant creation page", restaurant.getName());

            return "redirect:/manager/restaurants/create";
        }
    }

    /**
     * Displays create restaurant tables and times page.
     *
     * @param id    the restaurant id
     * @param model the model to bind attributes for the view
     * @return the tables and times creation page
     */
    @GetMapping("/create/{id}")
    public String getCreateTablesAndTimesPage(@PathVariable Long id,
                                              Model model) {
        logger.info("GET /manager/restaurants/create/{id} called");

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

        logger.info("Restaurant tables and times creation page rendered");

        return "manager/create_tables_and_times";
    }

    /**
     * Creates a new restaurant working time while creating a restaurant.
     *
     * @param id                 the restaurant id
     * @param newWorkingTime     the new working time
     * @param redirectAttributes the redirect attributes used to pass restaurant id on redirect
     * @return redirect to the tables and times creation page
     */
    @PostMapping("/create/{id}/addtime")
    public String addTimeToRestaurant(@PathVariable Long id,
                                      @ModelAttribute WorkingTime newWorkingTime,
                                      RedirectAttributes redirectAttributes) {
        logger.info("POST /manager/restaurants/create/{id}/addtime called");

        addTime(id, newWorkingTime, redirectAttributes);

        logger.info("Redirecting to tables and times creation page after adding time");

        return "redirect:/manager/restaurants/create/{id}";
    }

    /**
     * Creates a new restaurant table while creating a restaurant.
     *
     * @param id                 the restaurant id
     * @param newRestaurantTable the new restaurant table
     * @param tablesNumber       the number of tables
     * @param redirectAttributes the redirect attributes used to pass restaurant id on redirect
     * @return redirect to the tables and times creation page
     */
    @PostMapping("/create/{id}/addtable")
    public String addTableToRestaurant(@PathVariable Long id,
                                       @ModelAttribute RestaurantTable newRestaurantTable,
                                       @RequestParam Integer tablesNumber,
                                       RedirectAttributes redirectAttributes) {
        logger.info("POST /manager/restaurants/create/{id}/addtable called");

        addTable(id, newRestaurantTable, tablesNumber, redirectAttributes);

        logger.info("Redirecting to tables and times creation page after adding table");

        return "redirect:/manager/restaurants/create/{id}";
    }

    /**
     * Deletes the restaurant working time from creation.
     *
     * @param id                 the restaurant id
     * @param day                the day of the week
     * @param redirectAttributes the redirect attributes used to pass restaurant id on redirect
     * @return redirect to the tables and times creation page
     */
    @PostMapping("/create/{id}/deletetime/{day}")
    public String deleteTimeForCreateRestaurant(@PathVariable long id,
                                                @PathVariable DayOfWeek day,
                                                RedirectAttributes redirectAttributes) {
        logger.info("POST /manager/restaurants/create/{id}/deletetime/{day} called");

        workingTimeService.delete(id, day);

        redirectAttributes.addAttribute("id", id);

        logger.info("Redirecting to tables and times creation page after deleting time");

        return "redirect:/manager/restaurants/create/{id}";
    }

    /**
     * Deletes the restaurant table from creation.
     *
     * @param restaurantId       the restaurant id
     * @param tableId            the table id
     * @param redirectAttributes the redirect attributes used to pass flags if the table is deleted on redirect
     * @return redirect to the tables and times creation page
     */
    @PostMapping("/{restaurantId}/create/tables/{tableId}/delete")
    public String deleteTableForCreateRestaurant(@PathVariable long restaurantId,
                                                 @PathVariable long tableId,
                                                 RedirectAttributes redirectAttributes) {
        logger.info("POST /{restaurantId}/create/tables/{tableId}/delete called");

        if (!restaurantTableService.delete(tableId)) {
            redirectAttributes.addFlashAttribute("deleted", false);
        }

        logger.info("Redirecting to tables and times creation page after deleting table");

        return "redirect:/manager/restaurants/create/{restaurantId}";
    }

    /**
     * Displays edit restaurant page.
     *
     * @param id    the restaurant id
     * @param model the model to bind attributes for the view
     * @return the edit restaurant page view name
     */
    @GetMapping("/{id}/edit")
    public String getEditRestaurantPage(@PathVariable long id,
                                        Model model) {
        logger.info("GET /manager/restaurants/{id}/edit called");

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
        model.addAttribute("newWorkingTime", new WorkingTime(restaurant, null,
                null, null));
        model.addAttribute("newRestaurantTable", new RestaurantTable(restaurant, null));

        logger.info("Restaurant editing page rendered");

        return "manager/edit_restaurant";
    }

    /**
     * Updates the restaurant.
     *
     * @param newRestaurant      the new restaurant
     * @param id                 the restaurant id
     * @param redirectAttributes the redirect attributes used to pass restaurant id on redirect
     * @return redirect to the restaurant page
     */
    @PostMapping("/{id}/edit")
    public String editRestaurant(@ModelAttribute Restaurant newRestaurant,
                                 @PathVariable long id,
                                 RedirectAttributes redirectAttributes) {
        logger.info("POST /manager/restaurants/{id}/edit called");

        Restaurant restaurant = restaurantService.getRestaurant(id);
        restaurant.setName(newRestaurant.getName());
        restaurant.setAddress(newRestaurant.getAddress());
        restaurantService.updateRestaurant(restaurant);

        redirectAttributes.addAttribute("id", restaurant.getId());

        logger.info("Restaurant page rendered after update");

        return "redirect:/manager/restaurants/{id}";
    }

    /**
     * Creates a new working time while editing the restaurant.
     *
     * @param id                 the restaurant id
     * @param newWorkingTime     the new working time
     * @param redirectAttributes the redirect attributes used to pass restaurant id on redirect
     * @return redirect to the edit restaurant page
     */
    @PostMapping("/{id}/edit/addtime")
    public String editTimeForRestaurant(@PathVariable Long id,
                                        @ModelAttribute WorkingTime newWorkingTime,
                                        RedirectAttributes redirectAttributes) {
        logger.info("POST /manager/restaurants/{id}/edit/addtime called");

        addTime(id, newWorkingTime, redirectAttributes);

        logger.info("Redirecting to restaurant editing page after adding time");

        return "redirect:/manager/restaurants/{id}/edit";
    }

    /**
     * Creates a new restaurant table while editing the restaurant.
     *
     * @param id                 the restaurant id
     * @param newRestaurantTable the new restaurant table
     * @param tablesNumber       the number of tables
     * @param redirectAttributes the redirect attributes used to pass restaurant id on redirect
     * @return redirect to the edit restaurant page
     */
    @PostMapping("/{id}/edit/addtable")
    public String editTableForRestaurant(@PathVariable Long id,
                                         @ModelAttribute RestaurantTable newRestaurantTable,
                                         @RequestParam(required=false) Integer tablesNumber,
                                         RedirectAttributes redirectAttributes) {
        logger.info("POST /manager/restaurants/{id}/edit/addtable called");

        addTable(id, newRestaurantTable, tablesNumber, redirectAttributes);

        logger.info("Redirecting to restaurant editing page after adding table");

        return "redirect:/manager/restaurants/{id}/edit";
    }

    /**
     * Deletes the restaurant table from editing.
     *
     * @param restaurantId       the restaurant id
     * @param tableId            the table id
     * @param redirectAttributes the redirect attributes used to pass flags if the table is deleted on redirect
     * @return redirect to the edit restaurant page
     */
    @PostMapping("/{restaurantId}/edit/tables/{tableId}/delete")
    public String deleteTableForEditRestaurant(@PathVariable long restaurantId,
                                               @PathVariable long tableId,
                                               RedirectAttributes redirectAttributes) {
        logger.info("POST /{restaurantId}/edit/tables/{tableId}/delete called");

        if (!restaurantTableService.delete(tableId)) {
            redirectAttributes.addFlashAttribute("deleted", false);
        }

        logger.info("Redirecting to restaurant editing page after deleting table");

        return "redirect:/manager/restaurants/{restaurantId}/edit";
    }

    /**
     * Deletes the restaurant working time from editing.
     *
     * @param id                 the restaurant id
     * @param day                the day of the week
     * @param redirectAttributes the redirect attributes used to pass restaurant id on redirect
     * @return redirect to the edit restaurant page
     */
    @PostMapping("/{id}/edit/deletetime/{day}")
    public String deleteTimeForEditRestaurant(@PathVariable long id,
                                              @PathVariable DayOfWeek day,
                                              RedirectAttributes redirectAttributes) {
        logger.info("POST /manager/restaurants/{id}/edit/deletetime/{day} called");

        workingTimeService.delete(id, day);

        redirectAttributes.addAttribute("id", id);

        logger.info("Redirecting to restaurant editing page after deleting time");

        return "redirect:/manager/restaurants/{id}/edit";
    }

    /**
     * Deletes the restaurant.
     *
     * @param id the restaurant id
     * @return redirect to the list of all restaurants
     */
    @PostMapping("/{id}/delete")
    public String deleteRestaurant(@PathVariable long id) {
        logger.info("POST /manager/restaurants/{id}/delete called");

        restaurantService.deleteRestaurant(id);

        logger.info("Redirecting to all restaurants page after deleting restaurant");

        return "redirect:/manager/restaurants";
    }

    private void addTime(@PathVariable Long id,
                         @ModelAttribute WorkingTime newWorkingTime,
                         RedirectAttributes redirectAttributes) {
        Restaurant restaurant = restaurantService.getRestaurant(id);
        newWorkingTime.setRestaurant(restaurant);
        workingTimeService.createWorkingTime(new WorkingTime(restaurant, newWorkingTime.getDayOfWeek(),
                newWorkingTime.getOpenTime(), newWorkingTime.getCloseTime()));

        redirectAttributes.addAttribute("id", newWorkingTime.getRestaurant().getId());
    }

    private void addTable(@PathVariable Long id,
                          @ModelAttribute RestaurantTable newRestaurantTable,
                          @RequestParam(required = false) Integer tablesNumber,
                          RedirectAttributes redirectAttributes) {
        Restaurant restaurant = restaurantService.getRestaurant(id);
        newRestaurantTable.setRestaurant(restaurant);
        for (int i = 0; i < tablesNumber; i++) {
            restaurantTableService.createTable(new RestaurantTable(newRestaurantTable.getRestaurant(),
                    newRestaurantTable.getSeatsNumber()));
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
