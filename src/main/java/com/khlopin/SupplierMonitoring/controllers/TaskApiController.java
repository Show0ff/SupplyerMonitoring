package com.khlopin.SupplierMonitoring.controllers;

import com.khlopin.SupplierMonitoring.entity.TaskStatus;
import com.khlopin.SupplierMonitoring.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskApiController {

    private final TaskService taskService;

    @PutMapping("/{taskId}/status")
    public ResponseEntity<Void> updateTaskStatus(@PathVariable Long taskId, @RequestBody Map<String, String> statusPayload) {
        TaskStatus status = TaskStatus.valueOf(statusPayload.get("status"));
        taskService.updateTaskStatus(taskId, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
