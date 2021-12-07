package com.epam.rd.autocode.domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.epam.rd.autocode.Queries;
import com.epam.rd.autocode.service.Paging;

public class EmployeeDao {
    private static final String FIRST_NAME = "FIRSTNAME";
    private static final String LAST_NAME = "LASTNAME";
    private static final String MIDDLE_NAME = "MIDDLENAME";
    private static final String ID = "ID";
    private static final String POSITION = "POSITION";
    private static final String HIRE_DATE = "HIREDATE";
    private static final String SALARY = "SALARY";
    private static final String MANAGER = "MANAGER";
    private static final String DEPARTMENT = "DEPARTMENT";

    private final Connection conn;

    public EmployeeDao(Connection connection) {
        this.conn = connection;
    }

    public List<Employee> getAllWithSort(Paging paging, String sqlForSort) {
        try (PreparedStatement preparedStatement = conn.prepareStatement(sqlForSort)) {
            preparedStatement.setInt(1, paging.itemPerPage);
            preparedStatement.setInt(2, (paging.page - 1) * paging.itemPerPage);
            List<Employee> allEmployees = new ArrayList<>();
            ResultSet resultSetForPaging = preparedStatement.executeQuery();
            while (resultSetForPaging.next()) {
                allEmployees.add(convertDaraToEmployee(resultSetForPaging));
            }
            return allEmployees;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Employee convertDaraToEmployee(ResultSet resultSetForPaging) throws SQLException {
        DepartmentDao departmentDao = new DepartmentDao(conn);
        return new Employee(new BigInteger(String.valueOf(resultSetForPaging.getObject(ID))),
                createFullName(resultSetForPaging),
                Position.valueOf(resultSetForPaging.getString(POSITION)),
                resultSetForPaging.getDate(HIRE_DATE).toLocalDate(),
                new BigDecimal(String.valueOf(resultSetForPaging.getString(SALARY))),
                resultSetForPaging.getInt(MANAGER) == 0 ? null :
                        getById(resultSetForPaging.getInt(MANAGER)),
                resultSetForPaging.getBigDecimal(DEPARTMENT) == null ? null :
                        departmentDao.getDepartmentById(resultSetForPaging.getBigDecimal(DEPARTMENT).toBigInteger()));
    }

    public Employee getById(Integer Id) {
        try (final PreparedStatement preparedStatementForSearchEmployee = conn.prepareStatement(Queries.GET_EMPLOYEE_BY_ID);) {
            preparedStatementForSearchEmployee.setInt(1, Id);
            ResultSet resultSetForSearchEmployee = preparedStatementForSearchEmployee.executeQuery();
            resultSetForSearchEmployee.next();
            DepartmentDao departmentDao = new DepartmentDao(conn);
            return new Employee(new BigInteger(String.valueOf(resultSetForSearchEmployee.getLong(ID))),
                    createFullName(resultSetForSearchEmployee),
                    Position.valueOf(resultSetForSearchEmployee.getString(POSITION)),
                    resultSetForSearchEmployee.getDate(HIRE_DATE).toLocalDate(),
                    new BigDecimal(String.valueOf(resultSetForSearchEmployee.getString(SALARY))),
                    null,
                    resultSetForSearchEmployee.getBigDecimal(DEPARTMENT) == null ? null :
                            departmentDao.getDepartmentById(resultSetForSearchEmployee.getBigDecimal(DEPARTMENT).toBigInteger()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Employee> getAllByManagerWithSort(Paging paging, String sqlForSort, Employee manager) {
        try (PreparedStatement preparedStatement = conn.prepareStatement(sqlForSort)) {
            preparedStatement.setObject(1, manager.getId());
            preparedStatement.setInt(2, paging.itemPerPage);
            preparedStatement.setInt(3, (paging.page - 1) * paging.itemPerPage);
            List<Employee> allEmployees = new ArrayList<>();
            ResultSet resultSetForPaging = preparedStatement.executeQuery();
            while (resultSetForPaging.next()) {
                allEmployees.add(convertDaraToEmployee(resultSetForPaging));
            }
            return allEmployees;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Employee employeeWithFullManagerChain(Employee employee) {
        try (PreparedStatement preparedStatement = conn.prepareStatement(Queries.GET_ALL_EMPLOYEE, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Employee employeeWithManagers = new Employee(employee.getId(),
                    employee.getFullName(),
                    employee.getPosition(),
                    employee.getHired(),
                    employee.getSalary(),
                    employee.getManager() == null ? null :
                            createChainManager(resultSet, employee.getManager().getId().intValue(), null, resultSet.getRow()),
                    employee.getDepartment());
            return employeeWithManagers;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Employee createChainManager(ResultSet resultSet, Integer idManager, Employee employee, Integer numRow) {
        try {
            resultSet.beforeFirst();
            while (resultSet.next()) {
                if (resultSet.getInt(ID) == idManager) {
                    Integer nextIdManager = resultSet.getInt(MANAGER);
                    if (nextIdManager != 0) {
                        employee = createChainManager(resultSet, nextIdManager, employee, resultSet.getRow());
                    }
                    DepartmentDao departmentDao = new DepartmentDao(conn);
                    Employee manager = new Employee(resultSet.getBigDecimal(ID).toBigInteger(),
                            createFullName(resultSet),
                            Position.valueOf(resultSet.getString(POSITION)),
                            resultSet.getDate(HIRE_DATE).toLocalDate(),
                            new BigDecimal(String.valueOf(resultSet.getString(SALARY))),
                            employee,
                            resultSet.getBigDecimal(DEPARTMENT) == null ? null :
                                    departmentDao.getDepartmentById(resultSet.getBigDecimal(DEPARTMENT).toBigInteger()));
                    resultSet.absolute(numRow);
                    return manager;
                }
            }
            resultSet.absolute(numRow);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private FullName createFullName(ResultSet resultSet) {
        try {
            return new FullName(resultSet.getString(FIRST_NAME),
                    resultSet.getString(LAST_NAME),
                    resultSet.getString(MIDDLE_NAME));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
