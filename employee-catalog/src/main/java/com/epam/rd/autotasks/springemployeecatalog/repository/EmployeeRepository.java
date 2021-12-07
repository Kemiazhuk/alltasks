package com.epam.rd.autotasks.springemployeecatalog.repository;

import com.epam.rd.autotasks.springemployeecatalog.entity.DepartmentEntity;
import com.epam.rd.autotasks.springemployeecatalog.entity.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends CrudRepository<EmployeeEntity, Long> {
    List<EmployeeEntity> findAll(Pageable pageable);

    Page<EmployeeEntity> findEmployeeEntitiesByManagerIs(
            Long idManager,
            Pageable pageable);

    Page<EmployeeEntity> findEmployeeEntitiesByDepartmentIs(
            DepartmentEntity departmentEntity,
            Pageable pageable);
}
