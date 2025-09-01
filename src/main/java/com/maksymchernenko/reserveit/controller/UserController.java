package com.maksymchernenko.reserveit.controller;

import com.maksymchernenko.reserveit.exceptions.UserAlreadyExistsException;
import com.maksymchernenko.reserveit.exceptions.UserNotFoundException;
import com.maksymchernenko.reserveit.model.Role;
import com.maksymchernenko.reserveit.model.User;
import com.maksymchernenko.reserveit.model.dto.UserDTO;
import com.maksymchernenko.reserveit.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsible for handling user-related actions such as
 * registration, authentication, profile management, and administration tasks.
 * <p>
 * Provides endpoints for guests (registration, login), users (profile and
 * password management), and administrators (user management).
 */
@Controller
@RequestMapping()
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    /**
     * Instantiates a new User controller.
     *
     * @param userService the {@link UserService}
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the user registration page.
     *
     * @param model the model to bind attributes for the view
     * @return the registration page view name
     */
    @GetMapping("/guest/register")
    public String getRegisterPage(Model model) {
        logger.info("GET /guest/register called");

        model.addAttribute("user", new User());

        logger.info("Registration page rendered");

        return "guest/register";
    }

    /**
     * Processes a new user registration.
     *
     * @param user the user to be registered
     * @param redirectAttributes attributes used to pass error flags on redirect
     * @return redirect to a success page on success,
     * user registration page with an error if the user already exists otherwise
     */
    @PostMapping("/guest/register")
    public String registerUser(@ModelAttribute("user") User user,
                               RedirectAttributes redirectAttributes) {
        logger.info("POST /guest/register called");

        try {
            userService.register(user);

            logger.info("Successful registration page rendered");

            return "guest/successful_register";
        } catch (UserAlreadyExistsException e) {
            redirectAttributes.addAttribute("error", "true");

            logger.warn("Registration failed. User with email = {} already exists. " +
                    "Redirecting to registration page", user.getEmail());

            return "redirect:/guest/register";
        }
    }

    /**
     * Displays the user login page.
     *
     * @param model the model to bind attributes for the view
     * @return the login page view name
     */
    @GetMapping("/guest/login")
    public String loginUser(Model model) {
        logger.info("GET /guest/login called");

        model.addAttribute("user", new User());

        logger.info("Login page rendered");

        return "guest/login";
    }

    /**
     * Displays all registered users for admin.
     *
     * @param model the model to bind attributes for the view
     * @return the list of all users page view name
     */
    @GetMapping("/admin/users")
    public String getAllUsers(Model model) {
        logger.info("GET /admin/users called");

        model.addAttribute("users", userService.getAllUsers());

        logger.info("All users page rendered");

        return "admin/users";
    }

    /**
     * Displays a user creation page for admin.
     *
     * @param model the model to bind attributes for the view
     * @return the user creation page view name
     */
    @GetMapping("/admin/user/create")
    public String getCreateUserPage(Model model) {
        logger.info("GET /admin/user/create called");

        User user = new User();
        user.setRole(new Role());
        model.addAttribute("user", user);
        model.addAttribute("roles", userService.getAllRoles());

        logger.info("User creation page rendered");

        return "admin/create_user";
    }

    /**
     * Creates a new user. Redirects back to the list of users on success,
     * or to the creation page with an error on failure.
     *
     * @param user               the user to create
     * @param redirectAttributes the redirect attributes used to pass error flags on redirect
     * @return redirect to the list of all users page on success,
     * user creation page with an error if the user already exists otherwise
     */
    @PostMapping("/admin/user/create")
    public String createUser(@ModelAttribute User user,
                             RedirectAttributes redirectAttributes) {
        logger.info("POST /admin/user/create called");

        try {
            userService.register(user);

            logger.info("Redirecting to all users page");

            return "redirect:/admin/users";
        } catch (UserAlreadyExistsException e) {
            redirectAttributes.addAttribute("error", "true");

            logger.warn("User creation failed. User with email = {} already exists. " +
                    "Redirecting to user creation page", user.getEmail());

            return "redirect:/admin/user/create";
        }
    }

    /**
     * Deletes a user by id.
     *
     * @param id the user id
     * @return the list of all users page view name
     */
    @PostMapping("/admin/user/{id}/delete")
    public String deleteUser(@PathVariable("id") int id) {
        logger.info("POST /admin/user/{id}/delete called");

        userService.deleteUser(id);

        logger.info("Redirecting to all users page after deletion");

        return "redirect:/admin/users";
    }

    /**
     * Displays the currently authenticated user's profile page.
     *
     * @param model          the model to bind attributes for the view
     * @param authentication the authentication instance containing the current user
     * @return the user profile page view name on success,
     * redirect to logout if the user is not found otherwise
     */
    @GetMapping("/user/profile")
    public String getUserProfilePage(Model model,
                                     Authentication authentication) {
        logger.info("GET /user/profile called");

        String email = authentication.getName();
        try {
            User user = userService.getByEmail(email);
            model.addAttribute("firstName", user.getFirstName());
            model.addAttribute("lastName", user.getLastName());
            model.addAttribute("email", user.getEmail());

            logger.info("User profile page rendered");

            return "user/profile";
        } catch (UserNotFoundException e) {
            logger.warn("Getting user profile failed. User with email = {} does not exist. " +
                    "Redirecting to logout", authentication.getName());

            return "redirect:/user/logout";
        }
    }

    /**
     * Displays the edit profile page.
     *
     * @param model          the model to bind attributes for the view
     * @param authentication the authentication instance containing the current user
     * @return the edit profile page view name on success,
     * redirect to logout if the user is not found otherwise
     */
    @GetMapping("/user/profile/edit")
    public String getEditProfilePage(Model model,
                                     Authentication authentication) {
        logger.info("GET /user/profile/edit called");

        String email = authentication.getName();
        try {
            User user = userService.getByEmail(email);

            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());

            model.addAttribute("user", userDTO);

            logger.info("User profile editing page rendered");

            return "user/edit_profile";
        } catch (UserNotFoundException e) {
            logger.warn("Getting user profile editing page failed. User with email = {} does not exist. " +
                    "Redirecting to logout", authentication.getName());

            return "redirect:/user/logout";
        }
    }

    /**
     * Updates a user's first and/or last name.
     *
     * @param user           the updated user DTO
     * @param authentication the authentication instance containing the current user
     * @return redirect to the user profile page on success,
     * redirect to logout if the user is not found otherwise
     */
    @PostMapping("/user/profile/edit")
    public String editProfile(@ModelAttribute("user") UserDTO user,
                              Authentication authentication) {
        logger.info("POST /user/profile/edit called");

        try {
            User newUser = userService.getByEmail(authentication.getName());

            newUser.setFirstName(user.getFirstName());
            newUser.setLastName(user.getLastName());

            userService.updateUser(newUser);

            logger.info("Redirecting to user profile page");

            return "redirect:/user/profile";
        } catch (UserNotFoundException e) {
            logger.warn("User profile update failed. User with email = {} does not exist. " +
                    "Redirecting to logout", authentication.getName());

            return "redirect:/user/logout";
        }
    }

    /**
     * Changes user's password.
     *
     * @param oldPassword        the old password
     * @param newPassword        the new password
     * @param authentication     the authentication instance containing the current user
     * @param redirectAttributes the redirect attributes used to pass flags if the password is changed on redirect
     * @return redirect to the user edit profile page on success,
     * redirect to logout if the user is not found otherwise
     */
    @PostMapping("/user/profile/changepassword")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        logger.info("POST /user/profile/changepassword called");

        try {
            User user = userService.getByEmail(authentication.getName());
            boolean passwordChanged = userService.updatePassword(user, oldPassword, newPassword);

            redirectAttributes.addFlashAttribute("passwordChanged", passwordChanged);

            logger.info("Redirecting to user profile editing page after password change");

            return "redirect:/user/profile/edit";
        } catch (UserNotFoundException e) {
            logger.warn("User password change failed. User with email = {} does not exist. " +
                    "Redirecting to logout", authentication.getName());

            return "redirect:/user/logout";
        }
    }
}
