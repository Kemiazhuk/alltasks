package com.epam.rd.autocode;

public final class Queries {
    public static final String ALL_SORT_BY_HIRE_DATE = "Select * From Employee Order by HireDate LIMIT ? OFFSET ?";
    public static final String ALL_SORT_BY_LAST_NAME = "Select * From Employee Order by LastName LIMIT ? OFFSET ?";
    public static final String ALL_SORT_BE_SALARY = "Select * From Employee Order by Salary LIMIT ? OFFSET ?";
    public static final String ALL_SORT_BY_DEPARTMENT_NAME_AND_LAST_NAME = "Select * From Employee " +
            "Left Join Department On Employee.Department = Department.Id Order by Department.Name, Employee.LastName LIMIT ? OFFSET ?";
    public static final String GET_BY_DEPARTMENT_AND_SORT_HIRE_DATE = "Select * From Employee " +
            "Join Department On Employee.Department = Department.Id  Where Employee.Department = ? Order by Employee.HireDate LIMIT ? OFFSET ?";
    public static final String GET_BY_DEPARTMENT_AND_SORT_BY_SALARY = "Select * From Employee " +
            "Join Department On Employee.Department = Department.Id  Where Employee.Department = ? Order by Employee.Salary LIMIT ? OFFSET ?";
    public static final String GET_BY_DEPARTMENT_AND_SORT_LAST_NAME = "Select * From Employee " +
            "Join Department On Employee.Department = Department.Id  Where Employee.Department = ? Order by Employee.LastName LIMIT ? OFFSET ?";
    public static final String GET_BY_MANAGER_AND_SORT_LAST_NAME = "Select * From Employee " +
            "Join Department On Employee.Department = Department.Id  Where Employee.Manager = ? Order by Employee.LastName LIMIT ? OFFSET ?";
    public static final String GET_BY_MANAGER_AND_SORT_HIRE_DATE = "Select * From Employee " +
            "Join Department On Employee.Department = Department.Id  Where Employee.Manager = ? Order by Employee.HireDate LIMIT ? OFFSET ?";
    public static final String GET_BY_MANAGER_AND_SORT_SALARY = "Select * From Employee " +
            "Join Department On Employee.Department = Department.Id  Where Employee.Manager = ? Order by Employee.Salary LIMIT ? OFFSET ?";
    public static final String GET_BY_DEPARTMENT_AND_RANK_SALARY = "Select * From Employee " +
            "Join Department On Employee.Department = Department.Id  Where Employee.Department = ? Order by Employee.Salary DESC";
    public static final String GET_EMPLOYEE_BY_ID = "Select * From Employee Where ID = ?";
    public static final String GET_DEPARTMENT_BY_ID = "Select * From Department Where Id = ?";
    public static final String GET_ALL_EMPLOYEE = "Select * From Employee";
}
