package com.epam.rd.autocode.service;

import java.sql.SQLException;

public class ServiceFactory {

    public EmployeeService employeeService() throws SQLException {
        return new ServiceFactoryImpl();
    }
}
