package com.khlopin.SupplierMonitoring.services.repositories;

import com.khlopin.SupplierMonitoring.entity.CorpFile;
import com.khlopin.SupplierMonitoring.entity.Department;
import com.khlopin.SupplierMonitoring.entity.Project;
import com.khlopin.SupplierMonitoring.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface CorpFileRepository extends CrudRepository<CorpFile,Long> {


    Collection<? extends CorpFile> findByRecipientsContains(User user);

    Collection<? extends CorpFile> findByDepartmentRecipientsContains(Department userDepartment);

    Collection<? extends CorpFile> findByProjectRecipientsContains(Project userProject);
}
