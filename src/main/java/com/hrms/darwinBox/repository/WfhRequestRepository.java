package com.hrms.darwinBox.repository;

import com.hrms.darwinBox.model.WfhRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WfhRequestRepository extends JpaRepository<WfhRequest, Long> {
}
