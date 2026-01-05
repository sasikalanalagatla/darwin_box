package com.hrms.darwinBox.repository;

import com.hrms.darwinBox.enums.LeaveStatus;
import com.hrms.darwinBox.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    boolean existsByEmployeeIdAndFromDateLessThanEqualAndToDateGreaterThanEqualAndStatusIn(
            Long employeeId,
            LocalDate fromDate,
            LocalDate toDate,
            Collection<LeaveStatus> statuses);
}
