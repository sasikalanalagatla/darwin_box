package com.hrms.darwinBox.controller;

import com.hrms.darwinBox.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttendanceController {

    @Autowired
    AttendanceService attendanceService;
}
