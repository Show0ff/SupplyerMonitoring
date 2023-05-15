package com.khlopin.SupplierMonitoring.controllers;

import com.khlopin.SupplierMonitoring.entity.Project;
import com.khlopin.SupplierMonitoring.entity.Task;
import com.khlopin.SupplierMonitoring.entity.User;
import com.khlopin.SupplierMonitoring.services.ProjectService;
import com.khlopin.SupplierMonitoring.services.TaskService;
import com.khlopin.SupplierMonitoring.services.UserService;
import com.khlopin.SupplierMonitoring.services.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class ProjectBoardController {
    private final TaskService taskService;
    private final ProjectService projectService;

    private static final Logger log = LogManager.getLogger(ProjectBoardController.class);


    private final UserService userService;

    @GetMapping("/project-board")
    public String showProjectBoard(@RequestParam(required = false) Long projectId, Model model) {
        List<Task> tasks;
        if (projectId == null) {
            tasks = null;
        } else {
            tasks = taskService.getTasksByProjectId(projectId);
        }
        model.addAttribute("tasks", tasks);
        int projectProgress = projectService.getProjectProgress(projectId);
        model.addAttribute("projectProgress", projectProgress);
        model.addAttribute("projects", projectService.getAllProjects());
        return "project-board";
    }



    @GetMapping("/create-task")
    public String showCreateTaskForm(Model model) {
        model.addAttribute("newTask", new Task());
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("users", userService.getAllUsers());
        return "create-task";
    }


    @PostMapping("/create-task")
    public String createTask(@ModelAttribute Task newTask, @RequestParam Long projectId, @RequestParam Long worker, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Project project = projectService.getProjectById(projectId);
        User selectedWorker = userService.getUserById(worker);
        if (selectedWorker == null) {
            log.error("selected worker is null");
        } else if (project == null) {
            log.error("selected project is null");
        } else {
            selectedWorker.getWorkTask().add(newTask);
            newTask.setWorker(selectedWorker);
            taskService.createTask(newTask, user, project);
        }
        return "redirect:/project-board";
    }



}
