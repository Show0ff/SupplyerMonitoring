package com.khlopin.SupplierMonitoring.controllers;

import com.khlopin.SupplierMonitoring.entity.User;
import com.khlopin.SupplierMonitoring.services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user, Model model) {
        boolean isRegistered = userService.createUser(user);
        if (isRegistered) {
            model.addAttribute("message", "User registered successfully!");
            return "redirect:/";
        } else {
            model.addAttribute("error", "User already exists!");
            return "register";
        }
    }

    @GetMapping("/")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/")
    public String loginUser(@RequestParam String userName, @RequestParam String password, Model model, HttpSession session) {
        Optional<User> user = userService.authUser(userName, password);
        if (user.isPresent()) {
            session.setAttribute("user", user.get());
            return "redirect:/profile";
        } else {
            model.addAttribute("error", "Invalid credentials!");
            return "/";
        }
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/";
        }

        model.addAttribute("user", currentUser);
        return "profile";
    }

    @GetMapping("/logout")
    public String logoutUser(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }

}
