package com.khlopin.SupplierMonitoring.controllers;

import com.khlopin.SupplierMonitoring.entity.Department;
import com.khlopin.SupplierMonitoring.entity.User;
import com.khlopin.SupplierMonitoring.services.DepartmentService;
import com.khlopin.SupplierMonitoring.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;
    private final UserService userService;

    private static final Logger log = LogManager.getLogger(DepartmentController.class);

    @GetMapping("/departments")
    public String showDepartments(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("users", userService.getAllUsers());
        return "manage-department";
    }

    @PostMapping("/create-department")
    public String createDepartment(@RequestParam("name") String name) {
        Department department = new Department();
        department.setName(name);
        departmentService.createDepartment(department);
        log.info("Департамент " + department.getName() + " был создан");
        return "redirect:/departments";
    }

    @PostMapping("/add-remove-user-from-department")
    public String addRemoveUserFromDepartment(@RequestParam("userId") Long userId, @RequestParam("departmentId") Long departmentId, @RequestParam("action") String action) {
        User user = userService.getUserById(userId);
        Department department =  departmentService.getDepartmentById(departmentId);
        userService.addOrRemoveUserFromDepartment(action, user, department);
        return "redirect:/departments";
    }


}

