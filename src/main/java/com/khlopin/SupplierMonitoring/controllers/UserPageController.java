package com.khlopin.SupplierMonitoring.controllers;

import com.khlopin.SupplierMonitoring.entity.Task;
import com.khlopin.SupplierMonitoring.entity.User;
import com.khlopin.SupplierMonitoring.services.TaskService;
import com.khlopin.SupplierMonitoring.services.UserService;
import com.khlopin.SupplierMonitoring.services.repositories.UserRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserPageController {
    private final UserService userService;
    private final UserRepository userRepository;

    private final TaskService taskService;

    public UserPageController(UserService userService, UserRepository userRepository, TaskService taskService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public String showUserProfile(@PathVariable Long id, Model model) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
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
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            userService.uploadAvatar(file, byId.get());
            return "redirect:/user/{id}";
        } else {
            System.out.println("User with ID " + id + " not found");
            return "redirect:/login";
        }
    }

}

