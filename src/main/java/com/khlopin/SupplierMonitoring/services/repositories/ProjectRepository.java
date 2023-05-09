package com.khlopin.SupplierMonitoring.services.repositories;

import com.khlopin.SupplierMonitoring.entity.Project;
import com.khlopin.SupplierMonitoring.entity.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends CrudRepository<Project,Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Project p SET p.taskList = :taskList WHERE p.id = :projectId")
    void updateTaskList(@Param("projectId") Long projectId, @Param("taskList") List<Task> taskList);

}
