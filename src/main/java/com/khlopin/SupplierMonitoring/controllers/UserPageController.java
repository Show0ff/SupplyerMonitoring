package com.khlopin.SupplierMonitoring.controllers;

import com.khlopin.SupplierMonitoring.entity.Task;
import com.khlopin.SupplierMonitoring.entity.User;
import com.khlopin.SupplierMonitoring.services.TaskService;
import com.khlopin.SupplierMonitoring.services.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserPageController {
    private final UserService userService;
    private final TaskService taskService;

    @GetMapping("/{id}")
    public String showUserProfile(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        if (user != null) {
            model.addAttribute("user", user);

            List<Task> tasks = user.getWorkTask();
            tasks.addAll(user.getManageTasks());
            model.addAttribute("tasks", taskService.getAllTasks());


            return "profile";
        } else {
            System.out.println("User with ID " + id + " not found");
            return "redirect:/login";
        }
    }




    @PostMapping("/{id}/avatar")
    public String uploadUserProfile(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        User user = userService.getUserById(id);
        if (user != null) {
            userService.uploadAvatar(file, user);
            return "redirect:/user/{id}";
        } else {
            System.out.println("User with ID " + id + " not found");
            return "redirect:/login";
        }
    }

}

