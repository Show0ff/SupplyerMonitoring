package com.khlopin.SupplierMonitoring.services;

import com.khlopin.SupplierMonitoring.entity.*;
import com.khlopin.SupplierMonitoring.services.repositories.ProjectRepository;
import com.khlopin.SupplierMonitoring.services.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    private static final Logger log = LogManager.getLogger(ProjectService.class);

    @Transactional
    public Project addTaskInProject(Project project, Task task) {
        List<Task> taskList = project.getTaskList();
        if (taskList == null) {
            taskList = new ArrayList<>();
        }
        taskList.add(task);
        projectRepository.updateTaskList(project.getId(), taskList);
        return project;
    }


    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return (List<Project>) projectRepository.findAll();
    }

    public Project getProjectById(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isPresent()) {
            return project.get();
        } else {
            log.error(project + " not found");
            return null;
        }
    }

    public void addUserToProject(Project project, User user) {
        User userFromDB = userRepository.findByUserName(user.getUserName()).orElse(null);
        project.getUsersList().add(userFromDB);
        if (userFromDB != null) {
            userFromDB.setProject(project);
            projectRepository.save(project);
        }

    }


    public void removeUserFromProject(Project project, User user) {
        project.getUsersList().remove(user);
        projectRepository.save(project);
    }

    public List<User> getManagersFromProject(Project project) {
        return project.getUsersList().stream()
                .filter(user -> user.getRole() == Role.MANAGER)
                .collect(Collectors.toList());
    }

    public List<User> getWorkersFromProject(Project project) {
        return project.getUsersList().stream()
                .filter(user -> user.getRole() == Role.WORKER)
                .collect(Collectors.toList());
    }

    public List<User> getSuppliersFromProject(Project project) {
        return project.getUsersList().stream()
                .filter(user -> user.getRole() == Role.SUPPLIER)
                .collect(Collectors.toList());
    }
}
