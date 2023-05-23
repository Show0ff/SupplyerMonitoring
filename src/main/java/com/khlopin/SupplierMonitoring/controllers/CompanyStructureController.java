package com.khlopin.SupplierMonitoring.controllers;

import com.khlopin.SupplierMonitoring.entity.User;
import com.khlopin.SupplierMonitoring.services.DepartmentService;
import com.khlopin.SupplierMonitoring.services.ProjectService;
import com.khlopin.SupplierMonitoring.services.SupplierService;
import com.khlopin.SupplierMonitoring.services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CompanyStructureController {
    private final ProjectService projectService;
    private final SupplierService supplierService;
    private final UserService userService;
    private final DepartmentService departmentService;

    @GetMapping("/company-structure")
    public String showCustomerMenu(Model model) {

        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("departments", departmentService.getAllDepartments());

        return "company-structure";
    }

}
