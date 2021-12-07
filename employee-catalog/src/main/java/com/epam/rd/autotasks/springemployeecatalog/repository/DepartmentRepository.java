package com.epam.rd.autotasks.springemployeecatalog.repository;

import com.epam.rd.autotasks.springemployeecatalog.entity.DepartmentEntity;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentRepository extends CrudRepository<DepartmentEntity, Long> {
    DepartmentEntity findByDepartmentName(String departmentName);
    DepartmentEntity findByDepartmentId(Long departmentId);
}
