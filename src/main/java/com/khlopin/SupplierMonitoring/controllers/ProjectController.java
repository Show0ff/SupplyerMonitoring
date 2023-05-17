package com.khlopin.SupplierMonitoring.controllers;

import com.khlopin.SupplierMonitoring.entity.Project;
import com.khlopin.SupplierMonitoring.entity.Role;
import com.khlopin.SupplierMonitoring.entity.Supplier;
import com.khlopin.SupplierMonitoring.entity.User;
import com.khlopin.SupplierMonitoring.services.ProjectService;
import com.khlopin.SupplierMonitoring.services.SupplierService;
import com.khlopin.SupplierMonitoring.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private SupplierService supplierService;


    @GetMapping("/manage-projects")
    public String showManageProjects(Model model) {
        List<User> allUsers = userService.getAllUsers();
        List<User> customerList = new ArrayList<>();
        for (User user : allUsers) {
            if (user.getRole() == Role.CUSTOMER) {
                customerList.add(user);
            }
        }
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("customers", customerList);
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("users", allUsers);
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
        Project project = projectService.getProjectById(projectId);
        User user = userService.getUserById(userId);

        if (action.equals("add")) {
            projectService.addUserToProject(project, user);
        } else if (action.equals("remove")) {
            projectService.removeUserFromProject(project, user);
        }
        return "redirect:/manage-projects";
    }

    @PostMapping("/addCustomerInProject")
    public String addCustomerInProject(@RequestParam Long projectId, @RequestParam Long customerId, @RequestParam String action) {
        Project project = projectService.getProjectById(projectId);
        User customer = userService.getUserById(customerId);

        if (action.equals("add")) {
            projectService.addCustomerToProject(project, customer);
        } else if (action.equals("remove")) {
            projectService.removeCustomerFromProject(project, customer);
        }
        return "redirect:/manage-projects";
    }

    @PostMapping("/addSupplierInProject")
    public String addSupplierInProject(@RequestParam Long projectId, @RequestParam Long supplierId, @RequestParam String action) {
        Project project = projectService.getProjectById(projectId);
        Supplier supplier = supplierService.getSupplierById(supplierId);

        if (action.equals("add")) {
            projectService.addSupplierToProject(project, supplier);
        } else if (action.equals("remove")) {
            projectService.removeSupplierFromProject(project, supplier);
        }
        return "redirect:/manage-projects";
    }

    @PostMapping("/submit-extension")
    public String submitExtension(@RequestParam Long projectId, @RequestParam Integer extensionDays) {
        Project project = projectService.getProjectById(projectId);
        if (project != null && !project.isExtensionRequested()) {
            // Обновите дату сдачи проекта, добавив указанное количество дней
            LocalDate newDueDate = project.getProjectDueDate().plusDays(extensionDays);
            project.setProjectDueDate(newDueDate);
            project.setExtensionRequested(true);
            projectService.createProject(project);
        }
        return "redirect:/manage-projects";
    }





}
