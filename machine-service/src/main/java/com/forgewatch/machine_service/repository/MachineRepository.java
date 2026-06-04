package com.forgewatch.machine_service.repository;

/**
 * @author Md. Raihan Shikder (Raihan-89)
 */

import com.forgewatch.machine_service.entity.Machine;
import com.forgewatch.machine_service.enums.MachineStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MachineRepository extends JpaRepository<Machine, Long> {

    Optional<Machine> findByMachineCode(String machineCode);

    List<Machine> findByDepartment(String department);

    List<Machine> findByStatus(MachineStatus status);

    boolean existsByMachineCode(String machineCode);
}