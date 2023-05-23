package com.khlopin.SupplierMonitoring.controllers;

import com.khlopin.SupplierMonitoring.entity.Project;
import com.khlopin.SupplierMonitoring.entity.User;
import com.khlopin.SupplierMonitoring.services.ProjectService;
import com.khlopin.SupplierMonitoring.services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class CustomerMenuController {

    private final ProjectService projectService;
    private final UserService userService;

    @GetMapping("/customer-menu")
    public String showCustomerMenu(Model model, HttpSession httpSession) {
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);
        User user = userService.getUserById((Long) httpSession.getAttribute("userId"));
        model.addAttribute("user", user);
        int countApproveVotes = countApproveVotes(user.getProject().getCustomers());
        model.addAttribute("approveVotes", countApproveVotes);
        int countRejectVotes = countRejectVotes(user.getProject().getCustomers());
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
            if (allCustomersVoted(customers)) {
                int approveCount = countApproveVotes(customers);
                int rejectCount = countRejectVotes(customers);
                if (approveCount > rejectCount) {
                    // Одобрено большинством голосов
                    extendProjectDeadline(project);
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


    private boolean allCustomersVoted(List<User> customers) {
        for (User customer : customers) {
            if (customer.getExtensionApproval() == null) {
                return false; // Найден заказчик, который еще не проголосовал
            }
        }
        return true; // Все заказчики проголосовали
    }

    private int countApproveVotes(List<User> customers) {
        int count = 0;
        for (User customer : customers) {
            if (Boolean.TRUE.equals(customer.getExtensionApproval())) {
                count++;
            }
        }
        return count;
    }

    private int countRejectVotes(List<User> customers) {
        int count = 0;
        for (User customer : customers) {
            if (Boolean.FALSE.equals(customer.getExtensionApproval())) {
                count++;
            }
        }
        return count;
    }

    private void extendProjectDeadline(Project project) {
        // Получите текущую дату
        LocalDate currentDate = LocalDate.now();

        // если текущая дата меньше или равна дате сдачи проекта
        if (currentDate.isBefore(project.getProjectDueDate()) || currentDate.isEqual(project.getProjectDueDate())) {
            // максимальное значение даты среди голосов заказчиков
            LocalDate maxVoteDate = project.getCustomers().stream()
                    .map(User::getExtensionVoteDate)
                    .filter(Objects::nonNull)
                    .max(LocalDate::compareTo)
                    .orElse(currentDate); // Если нет голосов, использует текущую дату

            if (maxVoteDate.isAfter(currentDate) || maxVoteDate.isEqual(currentDate)) {
                // Установить новую дату сдачи проекта, добавив указанное количество дней к текущей дате
                LocalDate newDueDate = project.getProjectDueDate().plusDays(project.getExtensionDays());
                project.setProjectDueDate(newDueDate);
                projectService.createProject(project);
            }
        }
    }


}

