package com.forgewatch.shift_service.repository;

import com.forgewatch.shift_service.entity.Shift;
import com.forgewatch.shift_service.enums.ShiftType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Md. Raihan Shikder (Raihan-89)
 */
@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {

    List<Shift> findByDepartment(String department);

    List<Shift> findByShiftDate(LocalDate shiftDate);

    List<Shift> findByDepartmentAndShiftDate(String department, LocalDate shiftDate);

    List<Shift> findBySupervisorEmail(String supervisorEmail);
}