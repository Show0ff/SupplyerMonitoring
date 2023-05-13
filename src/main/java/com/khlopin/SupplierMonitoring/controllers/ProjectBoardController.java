package com.khlopin.SupplierMonitoring.controllers;

import com.khlopin.SupplierMonitoring.entity.Project;
import com.khlopin.SupplierMonitoring.entity.Task;
import com.khlopin.SupplierMonitoring.entity.User;
import com.khlopin.SupplierMonitoring.services.ProjectService;
import com.khlopin.SupplierMonitoring.services.TaskService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class ProjectBoardController {
    private final TaskService taskService;
    private final ProjectService projectService;

    @GetMapping("/project-board")
    public String showProjectBoard(@RequestParam(required = false) Long projectId, Model model) {
        List<Task> tasks;
        if (projectId == null) {
            tasks = null;
        } else {
            tasks = taskService.getTasksByProjectId(projectId);
        }
        model.addAttribute("tasks", tasks);
        model.addAttribute("projects", projectService.getAllProjects());
        return "project-board";
    }







    @GetMapping("/create-task")
    public String showCreateTaskForm(Model model) {
        model.addAttribute("newTask", new Task());
        model.addAttribute("projects", projectService.getAllProjects());
        return "create-task";
    }



    @PostMapping("/create-task")
    public String createTask(@ModelAttribute Task newTask, @RequestParam Long projectId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Project project = projectService.getProjectById(projectId);
        if (project == null) {
            // handle error, project not found
        } else {
            taskService.createTask(newTask, user, project);
        }
        return "redirect:/project-board";
    }







}
