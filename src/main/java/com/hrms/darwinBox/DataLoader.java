package com.hrms.darwinBox;

import com.hrms.darwinBox.enums.EmployeeStatus;
import com.hrms.darwinBox.enums.RoleType;
import com.hrms.darwinBox.model.Employee;
import com.hrms.darwinBox.model.EmployeeRole;
import com.hrms.darwinBox.model.Role;
import com.hrms.darwinBox.repository.EmployeeRepository;
import com.hrms.darwinBox.repository.EmployeeRoleRepository;
import com.hrms.darwinBox.repository.RoleRepository;
import com.hrms.darwinBox.service.AuditService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final EmployeeRoleRepository employeeRoleRepository;
    private final AuditService auditService;

    public DataLoader(EmployeeRepository employeeRepository,
            RoleRepository roleRepository,
            EmployeeRoleRepository employeeRoleRepository,
            AuditService auditService) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.employeeRoleRepository = employeeRoleRepository;
        this.auditService = auditService;
    }

    @Override
    public void run(String... args) throws Exception {

        // Ensure role records exist for all RoleType entries
        for (RoleType rt : RoleType.values()) {
            roleRepository.findByName(rt).orElseGet(() -> {
                Role r = new Role();
                r.setName(rt);
                return roleRepository.save(r);
            });
        }

        if (employeeRepository.count() == 0) {

            Employee admin = new Employee();
            admin.setEmployeeCode("ADMIN001");
            admin.setFullName("System Administrator");
            admin.setEmail("admin@example.com");
            admin.setDepartment("IT");
            admin.setDesignation("System Administrator");
            admin.setBaseMonthlySalary(BigDecimal.valueOf(100000));
            admin.setStatus(EmployeeStatus.ACTIVE);
            admin.setJoiningDate(LocalDate.now());
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());

            Employee savedAdmin = employeeRepository.save(admin);

            Role adminRole = roleRepository.findByName(RoleType.ADMIN)
                    .orElseThrow(() -> new RuntimeException("ADMIN role not found after seeding"));
            employeeRoleRepository.save(new EmployeeRole(savedAdmin, adminRole));

            auditService.log(
                    "Employee",
                    savedAdmin.getId(),
                    "CREATED",
                    savedAdmin.getId(),
                    "First system admin created on bootstrap");
            System.out.println("System Administrator created successfully!");
        }
    }
}
