package com.khlopin.SMTelegramBot.services;

import com.khlopin.SupplierMonitoring.entity.Task;
import com.khlopin.SupplierMonitoring.services.TaskService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class TelegramTaskService {
    private final TaskService taskService;


    //Получение информации о конкретной задаче по ее названию
    public Task getTaskForExtraInfo(String key) {
        Map<String, Task> taskMap = new HashMap<>();
        for (Task task : taskService.getAllTasks()) {
            taskMap.put(task.getText(), task);
        }
        return taskMap.get(key);
    }

    //Создание списка с названием задач
    public Set<String> getTaskSetList() {
        Set<String> taskExtraInfo = new HashSet<>();
        for (Task allTask : taskService.getAllTasks()) {
            taskExtraInfo.add(allTask.getText());
        }
        return taskExtraInfo;
    }
}
