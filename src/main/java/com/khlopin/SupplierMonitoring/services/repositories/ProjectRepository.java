package com.khlopin.SupplierMonitoring.services.repositories;

import com.khlopin.SupplierMonitoring.entity.Project;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProjectRepository extends CrudRepository<Project,Long> {


    List<Project> findProjectsByIdIn(List<Long> projectIds);

}
