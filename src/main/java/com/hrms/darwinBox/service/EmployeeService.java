package com.hrms.darwinBox.service;

import com.hrms.darwinBox.model.Employee;

public interface EmployeeService {
    Employee saveEmployee(Employee employee, Long creatorId, com.hrms.darwinBox.enums.RoleType role);
}
