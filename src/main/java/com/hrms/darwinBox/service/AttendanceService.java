package com.hrms.darwinBox.service;

import com.hrms.darwinBox.model.Attendance;

public interface AttendanceService {
    Attendance checkIn(Long employeeId, boolean wfh);

    Attendance checkOut(Long employeeId);
}
