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

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;


    @GetMapping("/")
    public String showLoginForm() {
        userService.createAdmin();
        return "login";
    }

    @PostMapping("/")
    public String loginUser(@RequestParam String userName, @RequestParam String password, Model model, HttpSession session) {
        Optional<User> user = userService.authUser(userName, password);
        if (user.isPresent()) {
            session.setAttribute("userId", user.get().getId());
            return "redirect:/profile";
        } else {
            model.addAttribute("error", "Неверный логин или пароль");
            return "/";
        }
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User currentUser = userService.getUserById((Long) session.getAttribute("userId"));
        if (currentUser == null) {
            return "redirect:/";
        }

        model.addAttribute("user", currentUser);
        return "profile";
    }



    @GetMapping("/logout")
    public String logoutUser(HttpSession session) {
        session.removeAttribute("userId");
        return "redirect:/";
    }

}
