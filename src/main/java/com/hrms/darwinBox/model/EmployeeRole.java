package com.hrms.darwinBox.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "employee_roles")
@Data
@AllArgsConstructor
public class EmployeeRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId;
    private Long roleId;

    public EmployeeRole() {
    }

    public EmployeeRole(Long employeeId, Long roleId) {
        this.employeeId = employeeId;
        this.roleId = roleId;
    }
}