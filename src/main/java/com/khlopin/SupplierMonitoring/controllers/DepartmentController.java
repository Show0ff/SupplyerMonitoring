package com.khlopin.SupplierMonitoring.controllers;

import com.khlopin.SupplierMonitoring.entity.Department;
import com.khlopin.SupplierMonitoring.entity.User;
import com.khlopin.SupplierMonitoring.services.DepartmentService;
import com.khlopin.SupplierMonitoring.services.UserService;
import com.khlopin.SupplierMonitoring.services.repositories.DepartmentRepository;
import com.khlopin.SupplierMonitoring.services.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

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
        return "redirect:/departments";
    }

    @PostMapping("/add-remove-user-from-department")
    public String addRemoveUserFromDepartment(@RequestParam("userId") Long userId, @RequestParam("departmentId") Long departmentId, @RequestParam("action") String action) {
        User user = userService.getUserById(userId);
        Department department = departmentRepository.findById(departmentId).orElse(null);

        if (user != null && department != null) {
            if (action.equals("add")) {
                department.getEmployees().add(user);
                user.setDepartment(department);
            } else if (action.equals("remove")) {
                department.getEmployees().remove(user);
                user.setDepartment(null);
            }
            departmentRepository.save(department);
            userRepository.save(user);
        }
        return "redirect:/departments";
    }
}

