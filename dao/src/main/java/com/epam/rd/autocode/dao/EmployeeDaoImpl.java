package com.epam.rd.autocode.dao;

import com.epam.rd.autocode.ConnectionSource;
import com.epam.rd.autocode.domain.Department;
import com.epam.rd.autocode.domain.Employee;
import com.epam.rd.autocode.domain.FullName;
import com.epam.rd.autocode.domain.Position;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDaoImpl implements EmployeeDao {

    private static final String FIRST_NAME = "FIRSTNAME";
    private static final String LAST_NAME = "LASTNAME";
    private static final String MIDDLE_NAME = "MIDDLENAME";
    private static final String ID = "ID";
    private static final String POSITION = "POSITION";
    private static final String HIRE_DATE = "HIREDATE";
    private static final String SALARY = "SALARY";
    private static final String MANAGER = "MANAGER";
    private static final String DEPARTMENT = "DEPARTMENT";
    private static final ConnectionSource CONNECTION_SOURCE = ConnectionSource.instance();
    private final Connection conn;

    private static final String GET_EMPLOYEE_BY_ID = "Select * From Employee Where ID = ?";
    private static final String GET_ALL_EMPLOYEES = "Select * From Employee";
    private static final String DELETE_EMPLOYEE = "Delete From Employee Where ID = ? and FIRSTNAME = ? and LASTNAME = ? and " +
            "MIDDLENAME = ? and POSITION = ? and MANAGER = ? and HIREDATE = ? and SALARY = ? and DEPARTMENT = ?";
    private static final String SAVE_EMPLOYEE = "INSERT INTO Employee ( ID, FIRSTNAME, LASTNAME, MIDDLENAME," +
            " POSITION, MANAGER, HIREDATE, SALARY, DEPARTMENT) VALUES(?,?,?,?,?,?,?,?,?)";
    private static final String GET_EMPLOYEES_BY_MANAGER = "Select  * From Employee Where Manager = ?";
    private static final String GET_EMPLOYEES_BY_DEPARTMENT = "Select  * From Employee Where Department = ?";

    public EmployeeDaoImpl() throws SQLException {
        this.conn = CONNECTION_SOURCE.createConnection();
    }

    @Override
    public List<Employee> getByDepartment(Department department) {
        try (final PreparedStatement preparedStatement = conn.prepareStatement(GET_EMPLOYEES_BY_DEPARTMENT);) {
            preparedStatement.setObject(1, department.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Employee> employeesInOneDepartment = new ArrayList<>();
            while (resultSet.next()) {
                employeesInOneDepartment.add(convertResultToEmployee(resultSet));
            }
            return employeesInOneDepartment;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Employee> getByManager(Employee employee) {
        List<Employee> employeesWithOneManager = new ArrayList<>();
        try (final PreparedStatement preparedStatement = conn.prepareStatement(GET_EMPLOYEES_BY_MANAGER);) {
            preparedStatement.setObject(1, employee.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                employeesWithOneManager.add(convertResultToEmployee(resultSet));
            }
            return employeesWithOneManager;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Employee> getById(BigInteger id) {
        try (final PreparedStatement preparedStatement = conn.prepareStatement(GET_EMPLOYEE_BY_ID);) {
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (id.compareTo(resultSet.getBigDecimal(ID).toBigInteger()) == 0) {
                    return Optional.of(convertResultToEmployee(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Employee> getAll() {
        List<Employee> allEmployees = new ArrayList<>();
        try (final Statement statement = conn.createStatement();) {
            ResultSet resultSet = statement.executeQuery(GET_ALL_EMPLOYEES);
            while (resultSet.next()) {
                allEmployees.add(convertResultToEmployee(resultSet));
            }
            return allEmployees;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Employee save(Employee employee) {
        try (final PreparedStatement preparedStatement = conn.prepareStatement(SAVE_EMPLOYEE);) {
            setAllParametersToPreparedStatement(employee, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employee;
    }

    @Override
    public void delete(Employee employee) {
        try (final PreparedStatement preparedStatement = conn.prepareStatement(DELETE_EMPLOYEE);) {
            setAllParametersToPreparedStatement(employee, preparedStatement);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setAllParametersToPreparedStatement(Employee employee, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setObject(1, employee.getId());
        preparedStatement.setString(2, employee.getFullName().getFirstName());
        preparedStatement.setString(3, employee.getFullName().getLastName());
        preparedStatement.setString(4, employee.getFullName().getMiddleName());
        preparedStatement.setString(5, employee.getPosition().toString());
        preparedStatement.setObject(6, employee.getManagerId());
        preparedStatement.setDate(7, Date.valueOf(employee.getHired()));
        preparedStatement.setBigDecimal(8, employee.getSalary());
        preparedStatement.setObject(9, employee.getDepartmentId());
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

    private Employee convertResultToEmployee(ResultSet resultSet) throws SQLException {
        return new Employee(
                resultSet.getBigDecimal(ID).toBigInteger(),
                createFullName(resultSet),
                Position.valueOf(resultSet.getString(POSITION)),
                resultSet.getDate(HIRE_DATE).toLocalDate(),
                resultSet.getBigDecimal(SALARY),
                BigInteger.valueOf(resultSet.getLong(MANAGER)),
                BigInteger.valueOf(resultSet.getLong(DEPARTMENT)));
    }
}