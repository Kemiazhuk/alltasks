package com.epam.rd.autocode.service;

import com.epam.rd.autocode.ConnectionSource;
import com.epam.rd.autocode.domain.Department;
import com.epam.rd.autocode.domain.DepartmentDao;
import com.epam.rd.autocode.domain.Employee;
import com.epam.rd.autocode.domain.EmployeeDao;
import com.epam.rd.autocode.Queries;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class ServiceFactoryImpl implements EmployeeService {

    private static final ConnectionSource CONNECTION_SOURCE = ConnectionSource.instance();
    private final EmployeeDao employeeDao;
    private final DepartmentDao departmentDao;


    public ServiceFactoryImpl() throws SQLException {
        Connection conn = CONNECTION_SOURCE.createConnection();
        this.employeeDao = new EmployeeDao(conn);
        this.departmentDao = new DepartmentDao(conn);
    }

    @Override
    public List<Employee> getAllSortByHireDate(Paging paging) {
        return employeeDao.getAllWithSort(paging, Queries.ALL_SORT_BY_HIRE_DATE);
    }

    @Override
    public List<Employee> getAllSortByLastname(Paging paging) {
        return employeeDao.getAllWithSort(paging, Queries.ALL_SORT_BY_LAST_NAME);
    }

    @Override
    public List<Employee> getAllSortBySalary(Paging paging) {
        return employeeDao.getAllWithSort(paging, Queries.ALL_SORT_BE_SALARY);
    }

    @Override
    public List<Employee> getAllSortByDepartmentNameAndLastname(Paging paging) {
        return employeeDao.getAllWithSort(paging, Queries.ALL_SORT_BY_DEPARTMENT_NAME_AND_LAST_NAME);
    }

    @Override
    public List<Employee> getByDepartmentSortByHireDate(Department department, Paging paging) {
        return departmentDao.
                getAllInDepartmentWithSortAndPaging(paging, Queries.GET_BY_DEPARTMENT_AND_SORT_HIRE_DATE, department);
    }

    @Override
    public List<Employee> getByDepartmentSortBySalary(Department department, Paging paging) {
        return departmentDao.
                getAllInDepartmentWithSortAndPaging(paging, Queries.GET_BY_DEPARTMENT_AND_SORT_BY_SALARY, department);
    }

    @Override
    public List<Employee> getByDepartmentSortByLastname(Department department, Paging paging) {
        return departmentDao.
                getAllInDepartmentWithSortAndPaging(paging, Queries.GET_BY_DEPARTMENT_AND_SORT_LAST_NAME, department);
    }

    @Override
    public List<Employee> getByManagerSortByLastname(Employee manager, Paging paging) {
        return employeeDao.getAllByManagerWithSort(paging, Queries.GET_BY_MANAGER_AND_SORT_LAST_NAME, manager);
    }

    @Override
    public List<Employee> getByManagerSortByHireDate(Employee manager, Paging paging) {
        return employeeDao.getAllByManagerWithSort(paging, Queries.GET_BY_MANAGER_AND_SORT_HIRE_DATE, manager);
    }

    @Override
    public List<Employee> getByManagerSortBySalary(Employee manager, Paging paging) {
        return employeeDao.getAllByManagerWithSort(paging, Queries.GET_BY_MANAGER_AND_SORT_SALARY, manager);
    }

    @Override
    public Employee getWithDepartmentAndFullManagerChain(Employee employee) {
        return employeeDao.employeeWithFullManagerChain(employee);
    }

    @Override
    public Employee getTopNthBySalaryByDepartment(int salaryRank, Department department) {
        return departmentDao.
                getAllInDepartmentWithSort(Queries.GET_BY_DEPARTMENT_AND_RANK_SALARY, department).get(salaryRank - 1);
    }
}
