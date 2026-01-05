package com.hrms.darwinBox.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "employee_roles")
@Data
public class EmployeeRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId;
    private Long roleId;
}