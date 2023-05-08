package com.khlopin.SupplierMonitoring.controllers;

import com.khlopin.SupplierMonitoring.entity.User;
import com.khlopin.SupplierMonitoring.services.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Component
@Controller
@RequestMapping("/user")
public class UserPageController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public String showUserProfile(@PathVariable Long id, Model model) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            model.addAttribute("user", user);
            return "redirect:/user/" + user.getId();
        } else {
            System.out.println("User with ID " + id + " not found");
            return "redirect:/login";
        }
    }


}
