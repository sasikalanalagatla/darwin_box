package com.hrms.darwinBox.model;

import com.hrms.darwinBox.enums.EmployeeStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "employees")
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String employeeCode;

    private String fullName;

    @Column(unique = true)
    private String email;

    private String designation;
    private String department;

    private Long managerId;

    private LocalDate joiningDate;

    private BigDecimal baseMonthlySalary;

    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}