package com.forgewatch.machine_service.repository;

import com.forgewatch.machine_service.entity.Machine;
import com.forgewatch.machine_service.enums.MachineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Md. Raihan Shikder (Raihan-89)
 */
@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {

    Optional<Machine> findByMachineCode(String machineCode);

    List<Machine> findByDepartment(String department);

    List<Machine> findByStatus(MachineStatus status);

    boolean existsByMachineCode(String machineCode);
}