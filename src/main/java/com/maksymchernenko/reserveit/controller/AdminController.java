package com.maksymchernenko.reserveit.controller;

import com.maksymchernenko.reserveit.exceptions.UserAlreadyExistsException;
import com.maksymchernenko.reserveit.model.Role;
import com.maksymchernenko.reserveit.model.User;
import com.maksymchernenko.reserveit.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", adminService.getAllUsers());

        return "admin/users";
    }

    @GetMapping("/user/create")
    public String getCreateUser(Model model) {
        User user = new User();
        user.setRole(new Role());
        model.addAttribute("user", user);
        model.addAttribute("roles", adminService.getAllRoles());

        return "admin/create_user";
    }

    @PostMapping("/user/create")
    public String createUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            adminService.createUser(user);

            return "redirect:/admin/users";
        } catch (UserAlreadyExistsException e) {
            redirectAttributes.addAttribute("error", "true");

            return "redirect:/admin/create_user";
        }
    }

    @PostMapping("/user/{id}/delete")
    public String deleteUser(@PathVariable("id") int id) {
        adminService.deleteUser(id);

        return "redirect:/admin/users";
    }
}
