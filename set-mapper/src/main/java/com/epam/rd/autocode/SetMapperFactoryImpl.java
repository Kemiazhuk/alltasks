package com.epam.rd.autocode;

import com.epam.rd.autocode.domain.Employee;
import com.epam.rd.autocode.domain.FullName;
import com.epam.rd.autocode.domain.Position;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class SetMapperFactoryImpl implements SetMapper<Set<Employee>> {
    public static final String FIRST_NAME = "FIRSTNAME";
    public static final String LAST_NAME = "LASTNAME";
    public static final String MIDDLE_NAME = "MIDDLENAME";
    public static final String ID = "ID";
    public static final String POSITION = "POSITION";
    public static final String HIRE_DATE = "HIREDATE";
    public static final String SALARY = "SALARY";
    public static final String MANAGER = "MANAGER";

    @Override
    public Set<Employee> mapSet(ResultSet resultSet) {
        Set<Employee> setEmployee = new HashSet<>();
        try {
            while (resultSet.next()) {
                Integer idManager = resultSet.getInt(MANAGER);
                int rowNumber = resultSet.getRow();
                setEmployee.add(createEmployee(resultSet, createManager(resultSet, idManager, null, resultSet.getRow())));
                resultSet.absolute(rowNumber);
            }
            return setEmployee;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Employee createManager(ResultSet resultSet, Integer idManager, Employee employee, Integer numRow) {
        try {
            resultSet.beforeFirst();
            while (resultSet.next()) {
                if (resultSet.getInt(ID) == idManager) {
                    Integer nextIdManager = resultSet.getInt(MANAGER);
                    if (nextIdManager != 0) {
                        employee = createManager(resultSet, nextIdManager, employee, resultSet.getRow());
                    }
                    Employee manager = createEmployee(resultSet, employee);
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

    private Employee createEmployee(ResultSet resultSet, Employee employee) {
        try {
            return new Employee(resultSet.getBigDecimal(ID).toBigInteger(),
                    createFullName(resultSet),
                    Position.valueOf(resultSet.getString(POSITION)),
                    resultSet.getDate(HIRE_DATE).toLocalDate(),
                    resultSet.getBigDecimal(SALARY),
                    employee);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
