package com.forgewatch.defect_service.repository;

import com.forgewatch.defect_service.entity.Defect;
import com.forgewatch.defect_service.enums.DefectSeverity;
import com.forgewatch.defect_service.enums.DefectStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DefectRepository extends JpaRepository<Defect, Long> {

    List<Defect> findByMachineCode(String machineCode);

    List<Defect> findByDepartment(String department);

    List<Defect> findBySeverity(DefectSeverity severity);

    List<Defect> findByStatus(DefectStatus status);

    List<Defect> findByReportedByEmail(String reportedByEmail);
}