package com.khlopin.SupplierMonitoring.controllers;

import com.khlopin.SupplierMonitoring.entity.Project;
import com.khlopin.SupplierMonitoring.entity.User;
import com.khlopin.SupplierMonitoring.services.ProjectService;
import com.khlopin.SupplierMonitoring.services.UserService;
import com.khlopin.SupplierMonitoring.services.VotingService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;


@Controller
@RequiredArgsConstructor
public class CustomerMenuController {

    private final ProjectService projectService;
    private final UserService userService;

    private final VotingService votingService;

    @GetMapping("/customer-menu")
    public String showCustomerMenu(Model model, HttpSession httpSession) {
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);
        User user = userService.getUserById((Long) httpSession.getAttribute("userId"));
        model.addAttribute("user", user);
        int countApproveVotes = votingService.countApproveVotes(user.getProject().getCustomers());
        model.addAttribute("approveVotes", countApproveVotes);
        int countRejectVotes = votingService.countRejectVotes(user.getProject().getCustomers());
        model.addAttribute("rejectVotes", countRejectVotes);
        return "customer-menu";
    }


    @PostMapping("/customer-menu/approve-extension")
    public String approveExtension(@RequestParam Long projectId, HttpSession httpSession) {
        Project project = projectService.getProjectById(projectId);
        if (project != null && project.isExtensionRequested() && !project.isExtensionPollCompleted()) {            // Здесь вы можете добавить логику для одобрения продления сроков проекта
            // Например, установить флаг одобрения для текущего пользователя
            // и проверить, все ли заказчики проголосовали
            // Если все заказчики проголосовали, выполнить необходимые действия
            List<User> customers = project.getCustomers();
            // Здесь предполагается, что текущий пользователь - заказчик проекта

            User currentUser = userService.getUserById((Long) httpSession.getAttribute("userId"));
            currentUser.setExtensionApproval(true); // Установить флаг одобрения для текущего пользователя
            if (votingService.allCustomersVoted(customers)) {
                int approveCount = votingService.countApproveVotes(customers);
                int rejectCount = votingService.countRejectVotes(customers);
                if (approveCount > rejectCount) {
                    // Одобрено большинством голосов
                    votingService.extendProjectDeadline(project);
                    // Выполнить необходимые действия
                }
                project.setExtensionPollCompleted(true); // Установить флаг завершения опроса
                projectService.createProject(project);
            }
        }
        return "redirect:/customer-menu";
    }

    @PostMapping("/customer-menu/reject-extension")
    public String rejectExtension(@RequestParam Long projectId) {
        Project project = projectService.getProjectById(projectId);
        if (project != null && project.isExtensionRequested()) {
            // Например, установить флаг отклонения для текущего пользователя и проверить, все ли заказчики отклонили продление
        }
        return "redirect:/customer-menu";
    }






}

