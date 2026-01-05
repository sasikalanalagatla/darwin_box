package com.hrms.darwinBox.controller;

import com.hrms.darwinBox.service.WfhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WfhController {

    @Autowired
    WfhService wfhService;
}
