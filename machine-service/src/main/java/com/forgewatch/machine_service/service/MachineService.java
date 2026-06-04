package com.forgewatch.machine_service.service;

import com.forgewatch.machine_service.dto.MachineRequest;
import com.forgewatch.machine_service.dto.MachineResponse;
import com.forgewatch.machine_service.entity.Machine;
import com.forgewatch.machine_service.enums.MachineStatus;
import com.forgewatch.machine_service.messaging.MachineEventPublisher;
import com.forgewatch.machine_service.repository.MachineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Md. Raihan Shikder (Raihan-89)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MachineService {

    private final MachineRepository machineRepository;

    private final MachineEventPublisher machineEventPublisher;

    public MachineResponse registerMachine(MachineRequest request) {

        if (machineRepository.existsByMachineCode(request.getMachineCode())) {
            throw new RuntimeException("Machine code already exists: "
                    + request.getMachineCode());
        }

        Machine machine = Machine.builder()
                .machineCode(request.getMachineCode())
                .machineName(request.getMachineName())
                .department(request.getDepartment())
                .location(request.getLocation())
                .description(request.getDescription())
                .status(MachineStatus.IDLE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Machine saved = machineRepository.save(machine);

        log.info("Machine registered: {}", saved.getMachineCode());

        return mapToResponse(saved);
    }

    public List<MachineResponse> getAllMachines() {
        return machineRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public MachineResponse getMachineById(Long id) {
        return mapToResponse(machineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Machine not found")));
    }

    public List<MachineResponse> getMachinesByDepartment(String department) {
        return machineRepository.findByDepartment(department)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<MachineResponse> getMachinesByStatus(MachineStatus status) {
        return machineRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public MachineResponse updateStatus(Long id, MachineStatus status) {

        Machine machine = machineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Machine not found"));

        machine.setStatus(status);
        machine.setUpdatedAt(LocalDateTime.now());

        if (status == MachineStatus.MAINTENANCE) {
            machine.setLastMaintenanceDate(LocalDateTime.now());
        }

        Machine saved = machineRepository.save(machine);

        MachineResponse response = mapToResponse(saved);

        if (status == MachineStatus.BREAKDOWN) {
            log.warn("Machine BREAKDOWN detected: {}", machine.getMachineCode());
            machineEventPublisher.publishMachineEvent(response);
        }

        return response;
    }

    public void deleteMachine(Long id) {
        machineRepository.deleteById(id);
    }

    private MachineResponse mapToResponse(Machine machine) {
        return MachineResponse.builder()
                .id(machine.getId())
                .machineCode(machine.getMachineCode())
                .machineName(machine.getMachineName())
                .department(machine.getDepartment())
                .location(machine.getLocation())
                .description(machine.getDescription())
                .status(machine.getStatus())
                .lastMaintenanceDate(machine.getLastMaintenanceDate())
                .createdAt(machine.getCreatedAt())
                .updatedAt(machine.getUpdatedAt())
                .build();
    }
}