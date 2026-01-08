package com.hrms.darwinBox.service.impl;

import com.hrms.darwinBox.service.AttendanceService;
import org.springframework.stereotype.Service;

import com.hrms.darwinBox.enums.AttendanceStatus;
import com.hrms.darwinBox.enums.EmployeeStatus;
import com.hrms.darwinBox.model.Attendance;
import com.hrms.darwinBox.model.Employee;
import com.hrms.darwinBox.repository.AttendanceRepository;
import com.hrms.darwinBox.repository.EmployeeRepository;
import com.hrms.darwinBox.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AuditService auditService;

    @Override
    public Attendance checkIn(Long employeeId, boolean wfh) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (employee.getStatus() != EmployeeStatus.ACTIVE) {
            throw new RuntimeException("Inactive employee cannot check-in");
        }

        LocalDate today = LocalDate.now();

        if (attendanceRepository.findByEmployeeIdAndDate(employeeId, today).isPresent()) {
            throw new RuntimeException("Already checked-in today");
        }

        Attendance attendance = new Attendance();
        attendance.setEmployeeId(employeeId);
        attendance.setDate(today);
        attendance.setPunchInTime(LocalTime.now());

        attendance.setStatus(wfh ? AttendanceStatus.WFH : AttendanceStatus.PRESENT);

        Attendance saved = attendanceRepository.save(attendance);

        auditService.log(
                "Attendance",
                saved.getId(),
                "CHECK_IN",
                employeeId,
                "Employee punched in"
        );

        return saved;
    }

    @Override
    public Attendance checkOut(Long employeeId) {

        LocalDate today = LocalDate.now();

        Attendance attendance = attendanceRepository
                .findByEmployeeIdAndDate(employeeId, today)
                .orElseThrow(() -> new RuntimeException("No check-in found for today"));

        if (attendance.getPunchOutTime() != null) {
            throw new RuntimeException("Already checked out");
        }

        LocalTime punchOut = LocalTime.now();
        attendance.setPunchOutTime(punchOut);

        long workedMinutes = java.time.Duration
                .between(attendance.getPunchInTime(), punchOut)
                .toMinutes();

        if (workedMinutes < 240) {
            attendance.setStatus(AttendanceStatus.HALF_DAY);
        }

        Attendance saved = attendanceRepository.save(attendance);

        auditService.log(
                "Attendance",
                saved.getId(),
                "CHECK_OUT",
                employeeId,
                "Employee punched out"
        );

        return saved;
    }
}