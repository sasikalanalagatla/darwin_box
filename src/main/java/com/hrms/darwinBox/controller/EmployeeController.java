package com.hrms.darwinBox.controller;

import com.hrms.darwinBox.model.Employee;
import com.hrms.darwinBox.service.EmployeeService;
import com.hrms.darwinBox.enums.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/add")
    public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee,
            @RequestParam Long id,
            @RequestParam RoleType role) {
        Employee savedEmployee = employeeService.saveEmployee(employee, id, role);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }

    @GetMapping("/")
    public ResponseEntity<Employee> getEmployeeByCode(@RequestParam String employeeCode) {
        Employee employee = employeeService.getEmployeeByCode(employeeCode);
        return ResponseEntity.ok(employee);
    }

    @PutMapping("/edit/{employeeCode}")
    public ResponseEntity<Employee> editEmployee(
            @PathVariable String employeeCode,
            @RequestBody Employee employee,
            @RequestParam Long actorId
    ) {
        Employee updated = employeeService.updateEmployee(employeeCode, employee, actorId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{employeeCode}")
    public ResponseEntity<String> deleteEmployee(
            @PathVariable String employeeCode,
            @RequestParam Long id
    ) {
        try {
            String res = employeeService.deleteEmployee(employeeCode, id);
            return ResponseEntity.ok(res);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
