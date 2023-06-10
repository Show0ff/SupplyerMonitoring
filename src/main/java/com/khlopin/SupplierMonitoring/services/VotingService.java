package com.khlopin.SupplierMonitoring.services;

import com.khlopin.SupplierMonitoring.entity.Project;
import com.khlopin.SupplierMonitoring.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class VotingService {

    private final ProjectService projectService;

    public VotingService(ProjectService projectService) {
        this.projectService = projectService;
    }


    public boolean allCustomersVoted(List<User> customers) {
        for (User customer : customers) {
            if (customer.getExtensionApproval() == null) {
                return false; // Найден заказчик, который еще не проголосовал
            }
        }
        return true; // Все заказчики проголосовали
    }

    public int countApproveVotes(List<User> customers) {
        int count = 0;
        for (User customer : customers) {
            if (Boolean.TRUE.equals(customer.getExtensionApproval())) {
                count++;
            }
        }
        return count;
    }

    public int countRejectVotes(List<User> customers) {
        int count = 0;
        for (User customer : customers) {
            if (Boolean.FALSE.equals(customer.getExtensionApproval())) {
                count++;
            }
        }
        return count;
    }

    public void extendProjectDeadline(Project project) {
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
