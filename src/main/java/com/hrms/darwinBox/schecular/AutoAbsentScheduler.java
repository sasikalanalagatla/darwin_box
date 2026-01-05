package com.hrms.darwinBox.schecular;

import com.hrms.darwinBox.enums.AttendanceStatus;
import com.hrms.darwinBox.enums.EmployeeStatus;
import com.hrms.darwinBox.model.Attendance;
import com.hrms.darwinBox.model.Employee;
import com.hrms.darwinBox.repository.AttendanceRepository;
import com.hrms.darwinBox.repository.LeaveRequestRepository;
import com.hrms.darwinBox.enums.LeaveStatus;
import com.hrms.darwinBox.repository.EmployeeRepository;
import com.hrms.darwinBox.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AutoAbsentScheduler {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final AuditService auditLogService;

    @Scheduled(cron = "0 59 23 * * ?")
    //@Scheduled(cron = "0 * * * * ?")
    public void autoMarkAbsent() {

        LocalDate today = LocalDate.now();

        List<Employee> activeEmployees = employeeRepository.findByStatus(EmployeeStatus.ACTIVE);

        for (Employee employee : activeEmployees) {

            boolean attendanceExists = attendanceRepository
                    .existsByEmployeeIdAndDate(employee.getId(), today);

            boolean leaveExists = leaveRequestRepository
                    .existsByEmployeeIdAndFromDateLessThanEqualAndToDateGreaterThanEqualAndStatusIn(
                            employee.getId(),
                            today,
                            today,
                            List.of(LeaveStatus.PENDING, LeaveStatus.APPROVED));

            if (!attendanceExists && !leaveExists) {

                Attendance attendance = new Attendance();
                attendance.setEmployeeId(employee.getId());
                attendance.setDate(today);
                attendance.setStatus(AttendanceStatus.ABSENT);
                attendance.setPunchInTime(null);
                attendance.setPunchOutTime(null);

                Attendance saved = attendanceRepository.save(attendance);

                auditLogService.log(
                        "Attendance",
                        saved.getId(),
                        "AUTO_ABSENT",
                        employee.getId(),
                        "Employee auto-marked ABSENT (no check-in, no leave)");
            }
        }
    }
}