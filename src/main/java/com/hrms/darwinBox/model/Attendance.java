package com.hrms.darwinBox.model;

import com.hrms.darwinBox.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "attendance")
@Data
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId;

    private LocalDate date;

    private LocalTime punchInTime;
    private LocalTime punchOutTime;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;
}