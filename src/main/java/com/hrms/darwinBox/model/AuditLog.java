package com.hrms.darwinBox.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String entityName;
    private Long entityId;

    private String action;

    private Long performedBy;

    private LocalDateTime performedAt;

    private String remarks;
}