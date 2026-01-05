package com.hrms.darwinBox.repository;

import com.hrms.darwinBox.model.EmployeeRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmployeeRoleRepository extends JpaRepository<EmployeeRole, Long> {
    List<EmployeeRole> findByEmployeeId(Long employeeId);

    boolean existsByEmployeeIdAndRoleId(Long employeeId, Long roleId);
}
