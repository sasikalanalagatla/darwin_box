package com.hrms.darwinBox.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "leave_balance")
@Data
public class LeaveBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId;

    private int year;

    private int totalAllowed = 20;

    private int usedLeaves;

    private int remainingLeaves;
}