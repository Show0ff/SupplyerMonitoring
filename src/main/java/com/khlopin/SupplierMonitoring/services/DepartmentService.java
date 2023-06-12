package com.khlopin.SupplierMonitoring.services;

import com.khlopin.SupplierMonitoring.entity.Department;
import com.khlopin.SupplierMonitoring.services.repositories.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    private static final Logger log = LogManager.getLogger(DepartmentService.class);


    public void createDepartment(Department department) {
        departmentRepository.save(department);
        log.info(department + " was created");

    }

    public List<Department> getAllDepartments() {
    return (List<Department>) departmentRepository.findAll();
    }
}
