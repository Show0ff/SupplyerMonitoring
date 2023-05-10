package com.khlopin.SupplierMonitoring.services;

import com.khlopin.SupplierMonitoring.entity.Task;
import com.khlopin.SupplierMonitoring.entity.TaskStatus;
import com.khlopin.SupplierMonitoring.entity.User;
import com.khlopin.SupplierMonitoring.services.repositories.TaskRepository;
import com.khlopin.SupplierMonitoring.services.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;
    public List<Task> getAllTasks() {
        return (List<Task>) taskRepository.findAll();
    }



    public Task createTask(Task task, User manager) {
        User managedUser = userRepository.findById(manager.getId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        task.setManager(managedUser);
        managedUser.getManageTasks().add(task);
        return taskRepository.save(task);
    }


    public void updateTaskStatus(Long taskId, TaskStatus status) {
        taskRepository.updateTaskStatus(taskId, status);
    }
}
