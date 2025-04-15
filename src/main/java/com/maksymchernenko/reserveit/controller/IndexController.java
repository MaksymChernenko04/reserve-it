package com.maksymchernenko.reserveit.controller;

import com.maksymchernenko.reserveit.model.User;
import com.maksymchernenko.reserveit.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {

    private final UserService userService;

    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String index(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User user = userService.getByEmail(email);
            model.addAttribute("fullName", user.getFirstName() + " " + user.getLastName());
        }

        return "index";
    }
}
