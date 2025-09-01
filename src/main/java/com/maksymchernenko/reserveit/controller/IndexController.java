package com.maksymchernenko.reserveit.controller;

import com.maksymchernenko.reserveit.exceptions.UserNotFoundException;
import com.maksymchernenko.reserveit.model.User;
import com.maksymchernenko.reserveit.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller responsible for handling a default page.
 * <p>
 * Provides an endpoint to render the home page for either a guest or a user.
 */
@Controller
@RequestMapping("/")
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    private final UserService userService;

    /**
     * Instantiates a new Index controller.
     *
     * @param userService the {@link UserService}
     */
    @Autowired
    public IndexController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the home page.
     *
     * @param model          the model to bind attributes for the view
     * @param authentication the authentication instance containing the current user
     * @return the user home page view name on success,
     * redirect to logout if the user is not found otherwise
     */
    @GetMapping
    public String index(Model model,
                        Authentication authentication) {
        logger.info("GET / called");

        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            try {
                User user = userService.getByEmail(email);
                model.addAttribute("fullName", user.getFirstName() + " " + user.getLastName());
            } catch (UserNotFoundException e) {
                logger.warn("User with email = {} does not exist. Redirecting to logout", email);

                return "redirect:/user/logout";
            }
        }

        logger.info("Index page rendered");

        return "index";
    }
}
