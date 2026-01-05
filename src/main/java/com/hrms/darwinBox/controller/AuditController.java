package com.hrms.darwinBox.controller;

import com.hrms.darwinBox.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuditController {

    @Autowired
    AuditService auditService;
}
