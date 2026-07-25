package com.forgewatch.shift_service.repository;

import com.forgewatch.shift_service.entity.Shift;
import com.forgewatch.shift_service.enums.ShiftType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Shift entity operations.
 *
 * @author Md. Raihan Shikder (Raihan-89)
 */
@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {

    Optional<Shift> findByPublicId(String publicId);

    List<Shift> findByDepartment(String department);

    List<Shift> findByShiftDate(LocalDate shiftDate);

    List<Shift> findByDepartmentAndShiftDate(String department, LocalDate shiftDate);

    List<Shift> findBySupervisorEmail(String supervisorEmail);
}
