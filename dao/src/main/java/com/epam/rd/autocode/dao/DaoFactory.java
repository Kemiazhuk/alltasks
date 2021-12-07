package com.epam.rd.autocode.dao;

import java.sql.SQLException;

public class DaoFactory {
    public EmployeeDao employeeDAO() throws SQLException {
        return new EmployeeDaoImpl();
    }

    public DepartmentDao departmentDAO() throws SQLException {
        return new DepartmentDaoImpl();
    }
}
