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
            throw new RuntimeException("Manager not found!");
        }
        // permission checks: creator must exist and have proper role
        var creatorOpt = employeeRepository.findById(creatorId);
        if (creatorOpt.isEmpty()) {
            throw new RuntimeException("Creator not found");
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
}
