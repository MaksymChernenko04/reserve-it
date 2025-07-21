package com.maksymchernenko.reserveit.controller;

import com.maksymchernenko.reserveit.exceptions.UserAlreadyExistsException;
import com.maksymchernenko.reserveit.model.Role;
import com.maksymchernenko.reserveit.model.User;
import com.maksymchernenko.reserveit.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/guest/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("user", new User());

        return "guest/register";
    }

    @PostMapping("/guest/register")
    public String registerUser(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        try {
            userService.register(user);

            return "guest/successful_register";
        } catch (UserAlreadyExistsException e) {
            redirectAttributes.addAttribute("error", "true");

            return "redirect:/guest/register";
        }
    }

    @GetMapping("/guest/login")
    public String loginUser(Model model) {
        model.addAttribute("user", new User());

        return "guest/login";
    }

    @PostMapping("/user/logout")
    public String logoutUser(HttpServletRequest req, HttpServletResponse res, Authentication auth) {
        new SecurityContextLogoutHandler().logout(req, res, auth);

        return "redirect:/";
    }

    @GetMapping("/admin/users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());

        return "admin/users";
    }

    @GetMapping("/admin/user/create")
    public String getCreateUserPage(Model model) {
        User user = new User();
        user.setRole(new Role());
        model.addAttribute("user", user);
        model.addAttribute("roles", userService.getAllRoles());

        return "admin/create_user";
    }

    @PostMapping("/admin/user/create")
    public String createUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userService.register(user);

            return "redirect:/admin/users";
        } catch (UserAlreadyExistsException e) {
            redirectAttributes.addAttribute("error", "true");

            return "redirect:/admin/user/create";
        }
    }

    @PostMapping("/admin/user/{id}/delete")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);

        return "redirect:/admin/users";
    }
}
