package com.hrms.darwinBox.service;

public interface AuditService {
    void log(String employee, Long id, String created, Long hrId, String newEmployeeAddedByHr);
}
