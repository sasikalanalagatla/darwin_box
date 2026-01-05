package com.hrms.darwinBox.model;

import com.hrms.darwinBox.enums.LeaveStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "wfh_requests")
@Data
public class WfhRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId;

    private LocalDate fromDate;
    private LocalDate toDate;

    private int numberOfDays;

    private String reason;

    @Enumerated(EnumType.STRING)
    private LeaveStatus status;

    private Long approvedBy;

    private LocalDateTime appliedAt;
}