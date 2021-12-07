package com.epam.rd.autocode;

import com.epam.rd.autocode.domain.Employee;
import com.epam.rd.autocode.domain.FullName;
import com.epam.rd.autocode.domain.Position;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RowMapperFactoryImpl implements RowMapper<Employee> {
    private static final String FIRST_NAME = "FIRSTNAME";
    private static final String LAST_NAME = "LASTNAME";
    private static final String MIDDLE_NAME = "MIDDLENAME";
    private static final String ID = "ID";
    private static final String POSITION = "POSITION";
    private static final String HIRE_DATE = "HIREDATE";
    private static final String SALARY = "SALARY";

    @Override
    public Employee mapRow(ResultSet resultSet) {
        try {
            return new Employee(resultSet.getBigDecimal(ID).toBigInteger(),
                    createFullName(resultSet),
                    Position.valueOf(resultSet.getString(POSITION)),
                    resultSet.getDate(HIRE_DATE).toLocalDate(),
                    resultSet.getBigDecimal(SALARY));
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
