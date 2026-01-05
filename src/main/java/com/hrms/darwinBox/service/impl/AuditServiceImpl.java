package com.hrms.darwinBox.service.impl;

import com.hrms.darwinBox.model.AuditLog;
import com.hrms.darwinBox.repository.AuditLogRepository;
import com.hrms.darwinBox.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditServiceImpl implements AuditService {
    @Autowired
    AuditLogRepository auditLogRepository;

    public void log(String entityName, Long entityId, String action, Long performedBy, String remarks) {
        AuditLog log = new AuditLog();
        log.setEntityName(entityName);
        log.setEntityId(entityId);
        log.setAction(action);
        log.setPerformedBy(performedBy);
        log.setRemarks(remarks);
        log.setPerformedAt(LocalDateTime.now());

        auditLogRepository.save(log);
    }
}
