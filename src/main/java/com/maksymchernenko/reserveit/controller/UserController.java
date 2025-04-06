package com.maksymchernenko.reserveit.controller;

import com.maksymchernenko.reserveit.exceptions.UserAlreadyExistsException;
import com.maksymchernenko.reserveit.exceptions.UserNotFoundException;
import com.maksymchernenko.reserveit.exceptions.WrongPasswordException;
import com.maksymchernenko.reserveit.model.User;
import com.maksymchernenko.reserveit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());

        return "user/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user) throws UserAlreadyExistsException {
        userService.register(user);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());

        return "user/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user) throws UserNotFoundException, WrongPasswordException {
        userService.login(user.getEmail(), user.getPassword());

        return "redirect:/";
    }
}
