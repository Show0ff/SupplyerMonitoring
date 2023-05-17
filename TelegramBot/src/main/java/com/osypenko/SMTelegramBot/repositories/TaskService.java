package com.osypenko.SMTelegramBot.repositories;

import com.osypenko.SMTelegramBot.entity.Task;
import com.osypenko.SMTelegramBot.repositories.interfaces.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {
    private final TaskRepository taskRepository;

    //Создание списка всех задач
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    //Получение информации о конкретной задаче по ее названию
    public Task getTaskForExtraInfo(String key) {
        Map<String, Task> taskMap = new HashMap<>();
        for (Task task : getAllTasks()) {
            taskMap.put(task.getExtraInfo(), task);
        }
        return taskMap.get(key);
    }

    //Создание списка с названием задач
    public Set<String> getTaskSetList() {
        Set<String> taskExtraInfo = new HashSet<>();
        for (Task allTask : getAllTasks()) {
            taskExtraInfo.add(allTask.getExtraInfo());
        }
        return taskExtraInfo;
    }
}
