package com.hrms.darwinBox.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Table(name = "audit_logs")
@Data
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String entityName;

    @Column(nullable = false)
    private Long entityId;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private Long performedBy;

    @Column(nullable = false)
    private LocalDateTime performedAt;

    private String remarks;

    @PrePersist
    public void onCreate() {
        this.performedAt = LocalDateTime.now();
    }
}