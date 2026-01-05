package com.hrms.darwinBox.controller;

import com.hrms.darwinBox.model.Attendance;
import com.hrms.darwinBox.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttendanceController {

    @Autowired
    AttendanceService attendanceService;

    @PostMapping("/check-in")
    public ResponseEntity<Attendance> checkIn(
            @RequestParam Long employeeId,
            @RequestParam(defaultValue = "false") boolean wfh
    ) {
        Attendance attendance = attendanceService.checkIn(employeeId, wfh);
        return ResponseEntity.ok(attendance);
    }

    @PostMapping("/check-out")
    public ResponseEntity<Attendance> checkOut(
            @RequestParam Long employeeId
    ){
        Attendance attendance = attendanceService.checkOut(employeeId);
        return ResponseEntity.ok(attendance);
    }
}
