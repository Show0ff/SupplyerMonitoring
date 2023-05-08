package com.khlopin.SupplierMonitoring.controllers;

import com.khlopin.SupplierMonitoring.entity.Task;
import com.khlopin.SupplierMonitoring.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class ProjectBoardController {
    private final TaskService taskService;

    @GetMapping("/project-board")
    public String showProjectBoard(Model model) {
        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);
        return "project-board";
    }


    @GetMapping("/create-task")
    public String showCreateTaskForm(Model model) {
        model.addAttribute("newTask", new Task());
        return "create-task";
    }

    @PostMapping("/create-task")
    public String createTask(Task newTask) {
        taskService.createTask(newTask);
        return "redirect:/project-board";
    }




}
