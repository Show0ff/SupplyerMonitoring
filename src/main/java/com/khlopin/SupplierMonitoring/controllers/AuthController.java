package com.khlopin.SupplierMonitoring.controllers;

import com.khlopin.SupplierMonitoring.entity.User;
import com.khlopin.SupplierMonitoring.services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    private static final Logger log = LogManager.getLogger(AuthController.class);


    @GetMapping("/")
    public String showLoginForm() {
        userService.createAdmin();
        return "login";
    }

    @PostMapping("/")
    public String loginUser(@RequestParam String userName, @RequestParam String password, RedirectAttributes redirectAttributes, HttpSession session) {
        Optional<User> user = userService.authUser(userName, password);
        if (user.isPresent()) {
            session.setAttribute("userId", user.get().getId());
            return "redirect:/profile";
        } else {
            redirectAttributes.addFlashAttribute("authError", "Неверный логин или пароль");
            return "redirect:/";
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
        log.info("Пользователь с id " + session.getAttribute("userId") + " log out");
        session.removeAttribute("userId");
        return "redirect:/";
    }

}
