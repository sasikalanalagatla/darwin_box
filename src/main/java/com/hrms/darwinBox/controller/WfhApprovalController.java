package com.hrms.darwinBox.controller;

import com.hrms.darwinBox.service.WfhApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WfhApprovalController {

    @Autowired
    WfhApprovalService wfhApprovalService;
}
