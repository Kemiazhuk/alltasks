package com.epam.rd.autocode.domain;

import com.epam.rd.autocode.Queries;
import com.epam.rd.autocode.service.Paging;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDao {

    private static final String NAME = "NAME";
    private static final String LOCATION = "LOCATION";
    private static final String ID = "ID";
    private final Connection conn;

    public DepartmentDao(Connection conn) {
        this.conn = conn;
    }

    public Department getDepartmentById(BigInteger departmentNum) {
        try (PreparedStatement preparedStatement = conn.prepareStatement(Queries.GET_DEPARTMENT_BY_ID)) {
            preparedStatement.setObject(1, departmentNum);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return new Department(new BigInteger(String.valueOf(resultSet.getLong(ID))),
                    resultSet.getString(NAME),
                    resultSet.getString(LOCATION));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Employee> getAllInDepartmentWithSort(String sqlForSort, Department department) {
        try (PreparedStatement preparedStatement = conn.prepareStatement(sqlForSort)) {
            preparedStatement.setObject(1, department.getId());
            List<Employee> allEmployees = new ArrayList<>();
            ResultSet resultSetForPaging = preparedStatement.executeQuery();
            EmployeeDao employeeDao = new EmployeeDao(conn);
            while (resultSetForPaging.next()) {
                allEmployees.add(employeeDao.convertDaraToEmployee(resultSetForPaging));
            }
            return allEmployees;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Employee> getAllInDepartmentWithSortAndPaging(Paging paging, String sqlForSort, Department department) {
        try (PreparedStatement preparedStatement = conn.prepareStatement(sqlForSort)) {
            preparedStatement.setObject(1, department.getId());
            preparedStatement.setInt(2, paging.itemPerPage);
            preparedStatement.setInt(3, (paging.page - 1) * paging.itemPerPage);
            List<Employee> allEmployees = new ArrayList<>();
            ResultSet resultSetForPaging = preparedStatement.executeQuery();
            EmployeeDao employeeDao = new EmployeeDao(conn);
            while (resultSetForPaging.next()) {
                allEmployees.add(employeeDao.convertDaraToEmployee(resultSetForPaging));
            }
            return allEmployees;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
