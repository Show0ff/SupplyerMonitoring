package com.khlopin.SupplierMonitoring.services.repositories;

import com.khlopin.SupplierMonitoring.entity.Department;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentRepository extends CrudRepository<Department,Long> {
}
