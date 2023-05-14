package com.khlopin.SupplierMonitoring.controllers;

import com.khlopin.SupplierMonitoring.entity.Role;
import com.khlopin.SupplierMonitoring.entity.User;
import com.khlopin.SupplierMonitoring.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserController {
@Autowired
    private final UserService userService;

    @GetMapping("/users")
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("newUser", new User());
        model.addAttribute("roles", Role.values());
        return "manage-users";
    }


    @PostMapping("/users")
    public String createUser(@ModelAttribute User newUser) {
        userService.createUser(newUser);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{userId}")
    public String updateUser(@PathVariable Long userId, @ModelAttribute User updatedUser) {
        userService.updateUser(userId, updatedUser);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/{userId}")
    public String showEditUserPage(@PathVariable Long userId, Model model) {
        User user = userService.getUserById(userId);

        if(user == null) {
            return "error-page";
        }

        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "edit-user";
    }

}
