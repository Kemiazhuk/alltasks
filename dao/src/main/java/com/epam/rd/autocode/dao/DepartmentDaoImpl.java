package com.epam.rd.autocode.dao;

import com.epam.rd.autocode.ConnectionSource;
import com.epam.rd.autocode.domain.Department;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DepartmentDaoImpl implements DepartmentDao {
    private static final String LOCATION = "LOCATION";
    private static final String NAME = "NAME";
    private static final String ID = "ID";
    private static final ConnectionSource CONNECTION_SOURCE = ConnectionSource.instance();
    private final Connection conn;

    private static final String GET_DEPARTMENT_BY_ID = "Select * From Department Where ID = ?";
    private static final String GET_ALL_DEPARTMENTS = "Select * From Department";
    private static final String DELETE_DEPARTMENT = "Delete From Department Where ID = ? and NAME = ? and LOCATION = ?";
    private static final String SAVE_DEPARTMENT = "INSERT INTO Department ( ID, NAME, LOCATION) VALUES(?,?,?)";
    private static final String UPDATE_DEPARTMENT = "UPDATE Department SET ID = ?, NAME = ?, LOCATION = ? Where ID = ?";

    public DepartmentDaoImpl() throws SQLException {
        this.conn = CONNECTION_SOURCE.createConnection();
    }

    @Override
    public Optional<Department> getById(BigInteger id) {
        try (PreparedStatement preparedStatement = conn.prepareStatement(GET_DEPARTMENT_BY_ID)) {
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (id.equals(resultSet.getBigDecimal(ID).toBigInteger())) {
                    return Optional.of(convertResultToDepartment(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Department> getAll() {
        try (Statement statement = conn.createStatement()) {
            List<Department> allDepartments = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(GET_ALL_DEPARTMENTS);
            while (resultSet.next()) {
                allDepartments.add(convertResultToDepartment(resultSet));
            }
            return allDepartments;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Department save(Department department) {
        if (getById(department.getId()).isEmpty()) {
            addNewDepartment(department);
        } else {
            updateDataInDepartment(department);
        }
        return department;
    }

    private void addNewDepartment(Department department) {
        try (PreparedStatement preparedStatement = conn.prepareStatement(SAVE_DEPARTMENT)) {
            setAllParametersInPreparedStatement(department, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Department updateDataInDepartment(Department department) {
        try (PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_DEPARTMENT)) {
            setAllParametersInPreparedStatement(department, preparedStatement);
            preparedStatement.setObject(4, department.getId());
            preparedStatement.executeUpdate();
            return department;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(Department department) {
        try (PreparedStatement preparedStatement = conn.prepareStatement(DELETE_DEPARTMENT)) {
            setAllParametersInPreparedStatement(department, preparedStatement);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setAllParametersInPreparedStatement(Department department, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setObject(1, department.getId());
        preparedStatement.setString(2, department.getName());
        preparedStatement.setString(3, department.getLocation());
    }

    private Department convertResultToDepartment(ResultSet resultSet) throws SQLException {
        return new Department(BigInteger.valueOf(resultSet.getLong(ID)),
                resultSet.getString(NAME),
                resultSet.getString(LOCATION));
    }
}
