package com.epam.rd.autotasks.springemployeecatalog.controller;

import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import com.epam.rd.autotasks.springemployeecatalog.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<Employee> showAllEmployees(Pageable pageable) {
        return employeeService.getAllEmployees(pageable);
    }

    @GetMapping("/{id}")
    public Employee showEmployeeById(
            @PathVariable("id") Long id,
            @RequestParam(value = "full_chain", defaultValue = "false")
                    Boolean fullChain) {
        return employeeService.getEmployeeById(id, fullChain);
    }

    @GetMapping("/by_manager/{managerId}")
    public List<Employee> showEmployeesByManagerId(
            @PathVariable("managerId") Long idManager,
            Pageable pageable) {
        return employeeService
                .getAllEmployeesByManagerId(idManager, pageable);
    }

    @GetMapping("/by_department/{department}")
    public List<Employee> showEmployeesForOneDepartment(
            @PathVariable("department") String department,
            Pageable pageable) {
        return employeeService
                .getAllEmployeesInDepartment(department, pageable);
    }
}

