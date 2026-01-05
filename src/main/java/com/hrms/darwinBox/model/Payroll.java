package com.hrms.darwinBox.model;

import com.hrms.darwinBox.enums.PayrollStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payroll")
@Data
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId;

    private int month;
    private int year;

    private BigDecimal baseSalary;

    private int totalLeavesTaken;
    private int extraLeaves;

    private BigDecimal perDaySalary;
    private BigDecimal salaryDeduction;
    private BigDecimal finalSalary;

    @Enumerated(EnumType.STRING)
    private PayrollStatus status;

    private LocalDateTime generatedAt;
}