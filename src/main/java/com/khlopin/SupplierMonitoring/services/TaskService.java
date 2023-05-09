package com.khlopin.SupplierMonitoring.services;

import com.khlopin.SupplierMonitoring.entity.Task;
import com.khlopin.SupplierMonitoring.entity.TaskStatus;
import com.khlopin.SupplierMonitoring.services.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    public List<Task> getAllTasks() {
        return (List<Task>) taskRepository.findAll();
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Transactional
    public void updateTaskStatus(Long taskId, TaskStatus status) {
        taskRepository.updateTaskStatus(taskId, status);
    }
}
