package com.epam.rd.autotasks.springemployeecatalog.services;

import com.epam.rd.autotasks.springemployeecatalog.domain.Department;
import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import com.epam.rd.autotasks.springemployeecatalog.domain.FullName;
import com.epam.rd.autotasks.springemployeecatalog.domain.Position;
import com.epam.rd.autotasks.springemployeecatalog.entity.DepartmentEntity;
import com.epam.rd.autotasks.springemployeecatalog.entity.EmployeeEntity;
import com.epam.rd.autotasks.springemployeecatalog.repository.DepartmentRepository;
import com.epam.rd.autotasks.springemployeecatalog.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository,
                           DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<Employee> getAllEmployees(Pageable pageable) {
        return employeeRepository
                .findAll(pageable)
                .stream()
                .map(employeeEntity -> convertDataToEmployee(employeeEntity,
                        true,
                        false))
                .collect(Collectors.toList());
    }

    public Employee getEmployeeById(Long id, Boolean fullChain) {
        Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(id);
        return employeeEntity.isEmpty()
                ? null
                : convertDataToEmployee(employeeEntity.get(), true, fullChain);
    }

    public List<Employee> getAllEmployeesByManagerId(Long managerId, Pageable pageable) {
        return employeeRepository
                .findEmployeeEntitiesByManagerIs(managerId, pageable)
                .stream()
                .map(employeeEntity -> convertDataToEmployee(employeeEntity,
                        true,
                        false))
                .collect(Collectors.toList());
    }

    public List<Employee> getAllEmployeesInDepartment(String department, Pageable pageable) {
        DepartmentEntity departmentEntity;
        if (isNumeric(department)) {
            departmentEntity = departmentRepository
                    .findByDepartmentId(Long.valueOf(department));
        } else {
            departmentEntity = departmentRepository
                    .findByDepartmentName(department);
        }
        return employeeRepository
                .findEmployeeEntitiesByDepartmentIs(departmentEntity, pageable)
                .stream()
                .map(employeeEntity -> convertDataToEmployee(employeeEntity,
                        true,
                        false))
                .collect(Collectors.toList());
    }

    private boolean isNumeric(String number) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        return pattern.matcher(number).matches();
    }

    private Employee convertDataToEmployee(
            EmployeeEntity employeeEntity,
            Boolean firstAppeal,
            Boolean managerChain) {

        Employee manager = null;

        if ((firstAppeal || managerChain) && employeeEntity.getManager() != null) {
            Optional<EmployeeEntity> entityManager = employeeRepository
                    .findById(employeeEntity.getManager());
            manager = entityManager
                    .map(value -> convertDataToEmployee(value, false, managerChain))
                    .orElse(null);
        }
        return new Employee(
                employeeEntity.getId(),
                convertDataToFullName(employeeEntity),
                Position.valueOf(employeeEntity.getPosition()),
                employeeEntity.getHired().toLocalDate(),
                BigDecimal.valueOf(employeeEntity.getSalary()),
                manager,
                convertDataToDepartment(employeeEntity.getDepartment()));
    }

    private FullName convertDataToFullName(EmployeeEntity employeeEntity) {
        return new FullName(employeeEntity.getFirstName(),
                employeeEntity.getLastName(),
                employeeEntity.getMeddleName());
    }

    private Department convertDataToDepartment(
            DepartmentEntity departmentEntity) {
        return departmentEntity != null
                ? new Department(departmentEntity.getDepartmentId(),
                departmentEntity.getDepartmentName(),
                departmentEntity.getLocation())
                : null;
    }
}
