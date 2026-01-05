package com.hrms.darwinBox.repository;

import com.hrms.darwinBox.enums.RoleType;
import com.hrms.darwinBox.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}
