package com.khlopin.SupplierMonitoring.services.repositories;

import com.khlopin.SupplierMonitoring.entity.Department;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DepartmentRepository extends CrudRepository<Department,Long> {
    List<Department> findDepartmentsByIdIn(List<Long> departmentIds);
}
