package com.hrms.darwinBox.service.impl;

import com.hrms.darwinBox.enums.RoleType;
import com.hrms.darwinBox.model.Employee;
import com.hrms.darwinBox.model.EmployeeRole;
import com.hrms.darwinBox.model.Role;
import com.hrms.darwinBox.repository.EmployeeRepository;
import com.hrms.darwinBox.repository.EmployeeRoleRepository;
import com.hrms.darwinBox.repository.RoleRepository;
import com.hrms.darwinBox.service.AuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceImplTest {

    EmployeeRepository employeeRepository;
    EmployeeRoleRepository employeeRoleRepository;
    RoleRepository roleRepository;
    AuditService auditService;

    EmployeeServiceImpl service;

    @BeforeEach
    void setUp() {
        employeeRepository = mock(EmployeeRepository.class);
        employeeRoleRepository = mock(EmployeeRoleRepository.class);
        roleRepository = mock(RoleRepository.class);
        auditService = mock(AuditService.class);

        service = new EmployeeServiceImpl();
        service.employeeRepository = employeeRepository;
        service.employeeRoleRepository = employeeRoleRepository;
        service.roleRepository = roleRepository;
        service.auditService = auditService;
    }

    @Test
    void adminCanCreateHr() {
        Employee creator = new Employee();
        creator.setId(1L);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(creator));
        Role adminRole = new Role();
        adminRole.setId(10L);
        adminRole.setName(RoleType.ADMIN);
        when(roleRepository.findById(10L)).thenReturn(Optional.of(adminRole));
        com.hrms.darwinBox.model.Employee creatorEmp = new com.hrms.darwinBox.model.Employee();
        creatorEmp.setId(1L);
        when(employeeRoleRepository.findByEmployee_Id(1L))
                .thenReturn(List.of(new com.hrms.darwinBox.model.EmployeeRole(creatorEmp, adminRole)));

        Role hrRole = new Role();
        hrRole.setId(20L);
        hrRole.setName(RoleType.HR);
        when(roleRepository.findByName(RoleType.HR)).thenReturn(Optional.of(hrRole));

        Employee newEmp = new Employee();
        newEmp.setEmployeeCode("HR100");
        newEmp.setEmail("hr100@example.com");

        when(employeeRepository.existsByEmployeeCode("HR100")).thenReturn(false);
        when(employeeRepository.findByEmail("hr100@example.com")).thenReturn(Optional.empty());

        when(employeeRepository.save(any(Employee.class))).thenAnswer(inv -> {
            Employee e = inv.getArgument(0);
            e.setId(2L);
            return e;
        });

        when(employeeRoleRepository.existsByEmployee_IdAndRole_Id(2L, 20L)).thenReturn(false);

        var saved = service.saveEmployee(newEmp, 1L, RoleType.HR);
        assertNotNull(saved);
        assertEquals(2L, saved.getId());

        verify(employeeRoleRepository).save(any(EmployeeRole.class));
    }

    @Test
    void nonAdminCannotCreateHr() {
        Employee creator = new Employee();
        creator.setId(5L);

        when(employeeRepository.findById(5L)).thenReturn(Optional.of(creator));
        // creator has no roles
        when(employeeRoleRepository.findByEmployee_Id(5L)).thenReturn(List.of());

        Employee newEmp = new Employee();
        newEmp.setEmployeeCode("HR101");
        newEmp.setEmail("hr101@example.com");

        when(employeeRepository.existsByEmployeeCode("HR101")).thenReturn(false);
        when(employeeRepository.findByEmail("hr101@example.com")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.saveEmployee(newEmp, 5L, RoleType.HR));
        assertTrue(ex.getMessage().contains("Only ADMIN"));
    }

    @Test
    void hrCanCreateEmployee() {
        Employee creator = new Employee();
        creator.setId(11L);
        when(employeeRepository.findById(11L)).thenReturn(Optional.of(creator));

        Role hrRole = new Role();
        hrRole.setId(30L);
        hrRole.setName(RoleType.HR);
        when(roleRepository.findById(30L)).thenReturn(Optional.of(hrRole));
        com.hrms.darwinBox.model.Employee hrEmp = new com.hrms.darwinBox.model.Employee();
        hrEmp.setId(11L);
        when(employeeRoleRepository.findByEmployee_Id(11L))
                .thenReturn(List.of(new com.hrms.darwinBox.model.EmployeeRole(hrEmp, hrRole)));

        Role empRole = new Role();
        empRole.setId(40L);
        empRole.setName(RoleType.EMPLOYEE);
        when(roleRepository.findByName(RoleType.EMPLOYEE)).thenReturn(Optional.of(empRole));

        Employee newEmp = new Employee();
        newEmp.setEmployeeCode("EMP200");
        newEmp.setEmail("emp200@example.com");
        when(employeeRepository.existsByEmployeeCode("EMP200")).thenReturn(false);
        when(employeeRepository.findByEmail("emp200@example.com")).thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenAnswer(inv -> {
            Employee e = inv.getArgument(0);
            e.setId(12L);
            return e;
        });

        var saved = service.saveEmployee(newEmp, 11L, RoleType.EMPLOYEE);
        assertEquals(12L, saved.getId());
        verify(employeeRoleRepository).save(any(EmployeeRole.class));
    }

    @Test
    void missingCreatorThrows() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());
        Employee newEmp = new Employee();
        newEmp.setEmployeeCode("X");
        newEmp.setEmail("x@example.com");
        when(employeeRepository.existsByEmployeeCode("X")).thenReturn(false);
        when(employeeRepository.findByEmail("x@example.com")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.saveEmployee(newEmp, 999L, RoleType.EMPLOYEE));
        assertTrue(ex.getMessage().contains("Creator not found"));
    }
}
