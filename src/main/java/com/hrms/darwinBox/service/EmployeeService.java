package com.hrms.darwinBox.service;

import com.hrms.darwinBox.enums.RoleType;
import com.hrms.darwinBox.model.Employee;

public interface EmployeeService {
    Employee saveEmployee(Employee employee, Long creatorId, RoleType role);
    Employee getEmployeeByCode(String employeeCode);
    Employee updateEmployee(String employeeCode, Employee employee, Long actorId);
    String deleteEmployee(String employeeCode, Long Id);
}
