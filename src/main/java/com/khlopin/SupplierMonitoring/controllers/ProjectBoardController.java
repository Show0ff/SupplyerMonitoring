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
    public String createTask(Project project, Task newTask, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Task task = taskService.createTask(newTask,user);
        projectService.addTaskInProject(project, task);
        return "redirect:/project-board";
    }

}
