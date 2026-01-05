package com.hrms.darwinBox.service.impl;

import com.hrms.darwinBox.enums.EmployeeStatus;
import com.hrms.darwinBox.enums.RoleType;
import com.hrms.darwinBox.model.Employee;
import com.hrms.darwinBox.repository.EmployeeRepository;
import com.hrms.darwinBox.model.EmployeeRole;
import com.hrms.darwinBox.model.Role;
import com.hrms.darwinBox.repository.EmployeeRoleRepository;
import com.hrms.darwinBox.repository.RoleRepository;
import com.hrms.darwinBox.service.AuditService;
import com.hrms.darwinBox.service.EmployeeService;
import com.hrms.darwinBox.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    AuditService auditService;
    @Autowired
    EmployeeRoleRepository employeeRoleRepository;
    @Autowired
    RoleRepository roleRepository;

    @Override
    public Employee saveEmployee(Employee employee, Long creatorId, RoleType role) {
        if (employeeRepository.existsByEmployeeCode(employee.getEmployeeCode())) {
            throw new RuntimeException("Employee code already exists!");
        }
        if (employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }

        if (employee.getManagerId() != null &&
                employeeRepository.findById(employee.getManagerId()).isEmpty()) {
            throw new ResourceNotFoundException("Manager not found");
        }
        // permission checks: creator must exist and have proper role
        var creatorOpt = employeeRepository.findById(creatorId);
        if (creatorOpt.isEmpty()) {
            throw new ResourceNotFoundException("Creator not found");
        }

        var creatorRoles = employeeRoleRepository.findByEmployeeId(creatorId);
        boolean creatorIsAdmin = false;
        boolean creatorIsHr = false;
        for (EmployeeRole er : creatorRoles) {
            Role r = roleRepository.findById(er.getRoleId()).orElse(null);
            if (r != null) {
                if (r.getName() == RoleType.ADMIN)
                    creatorIsAdmin = true;
                if (r.getName() == RoleType.HR)
                    creatorIsHr = true;
            }
        }

        if (role == RoleType.HR) {
            if (!creatorIsAdmin) {
                throw new RuntimeException("Only ADMIN can create HR users");
            }
        } else {
            if (!creatorIsHr && !creatorIsAdmin) { // allow admin also to add any
                throw new RuntimeException("Only HR (or ADMIN) can create employees of this type");
            }
        }

        employee.setStatus(EmployeeStatus.ACTIVE);

        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());
        if (employee.getJoiningDate() == null) {
            employee.setJoiningDate(LocalDate.now());
        }

        Employee savedEmployee = employeeRepository.save(employee);

        // assign role
        Role targetRole = roleRepository.findByName(role)
                .orElseThrow(() -> new RuntimeException("Target role not found"));
        if (!employeeRoleRepository.existsByEmployeeIdAndRoleId(savedEmployee.getId(), targetRole.getId())) {
            employeeRoleRepository.save(new EmployeeRole(savedEmployee.getId(), targetRole.getId()));
        }

        auditService.log(
                "Employee",
                savedEmployee.getId(),
                "CREATED",
                creatorId,
                "New employee added");

        return savedEmployee;
    }

    @Override
    public Employee getEmployeeByCode(String employeeCode) {
        return employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with code: " + employeeCode));
    }

    @Override
    public Employee updateEmployee(String employeeCode, Employee updatedEmployee, Long actorId) {

        // ensure actor exists
        var actorOpt = employeeRepository.findById(actorId);
        if (actorOpt.isEmpty()) {
            throw new com.hrms.darwinBox.exception.ResourceNotFoundException("Actor not found");
        }

        // check actor roles by joining employeeRole -> role
        boolean isAdmin = false;
        boolean isHr = false;
        var actorRoles = employeeRoleRepository.findByEmployeeId(actorId);
        for (var er : actorRoles) {
            var role = roleRepository.findById(er.getRoleId()).orElse(null);
            if (role != null) {
                if (role.getName() == RoleType.ADMIN)
                    isAdmin = true;
                if (role.getName() == RoleType.HR)
                    isHr = true;
            }
        }

        if (!isAdmin && !isHr) {
            throw new com.hrms.darwinBox.exception.ForbiddenException("Only ADMIN or HR can edit employee details");
        }

        Employee existingEmployee = employeeRepository
                .findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new com.hrms.darwinBox.exception.ResourceNotFoundException("Employee not found"));

        // copy only fields provided in request (partial update)
        if (updatedEmployee.getFullName() != null) {
            existingEmployee.setFullName(updatedEmployee.getFullName());
        }
        if (updatedEmployee.getDesignation() != null) {
            existingEmployee.setDesignation(updatedEmployee.getDesignation());
        }
        if (updatedEmployee.getDepartment() != null) {
            existingEmployee.setDepartment(updatedEmployee.getDepartment());
        }
        if (updatedEmployee.getManagerId() != null) {
            existingEmployee.setManagerId(updatedEmployee.getManagerId());
        }
        if (updatedEmployee.getBaseMonthlySalary() != null) {
            existingEmployee.setBaseMonthlySalary(updatedEmployee.getBaseMonthlySalary());
        }
        if (updatedEmployee.getStatus() != null) {
            existingEmployee.setStatus(updatedEmployee.getStatus());
        }
        if (updatedEmployee.getJoiningDate() != null) {
            existingEmployee.setJoiningDate(updatedEmployee.getJoiningDate());
        }

        existingEmployee.setUpdatedAt(LocalDateTime.now());

        Employee savedEmployee = employeeRepository.save(existingEmployee);

        auditService.log(
                "Employee",
                savedEmployee.getId(),
                "UPDATED",
                actorId,
                "Employee details updated");

        return savedEmployee;
    }
}
