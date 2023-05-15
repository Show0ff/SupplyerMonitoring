package com.khlopin.SupplierMonitoring.controllers;

import com.khlopin.SupplierMonitoring.entity.Project;
import com.khlopin.SupplierMonitoring.entity.User;
import com.khlopin.SupplierMonitoring.services.ProjectService;
import com.khlopin.SupplierMonitoring.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;


    @GetMapping("/manage-projects")
    public String showManageProjects(Model model) {
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("users", userService.getAllUsers());  // assuming you have a UserService
        model.addAttribute("newProject", new Project());
        return "manage-projects";
    }




    @PostMapping("/manage-projects")
    public String createSupplier(Project project) {
        projectService.createProject(project);
        return "redirect:/manage-projects";
    }

    @PostMapping("/addUserInProject")
    public String addUserInProject(@RequestParam Long projectId, @RequestParam Long userId, @RequestParam String action) {
        Project project =  projectService.getProjectById(projectId);
        User user =  userService.getUserById(userId);

        if (action.equals("add")) {
            projectService.addUserToProject(project, user);
        } else if (action.equals("remove")) {
            projectService.removeUserFromProject(project, user);
        }
        return "redirect:/manage-projects";
    }



}
