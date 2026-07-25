package com.forgewatch.machine_service.service;

import com.forgewatch.common.exception.DuplicateResourceException;
import com.forgewatch.common.exception.InvalidOperationException;
import com.forgewatch.common.exception.ResourceNotFoundException;
import com.forgewatch.machine_service.dto.MachineRequest;
import com.forgewatch.machine_service.dto.MachineResponse;
import com.forgewatch.machine_service.entity.Machine;
import com.forgewatch.machine_service.enums.MachineStatus;
import com.forgewatch.machine_service.messaging.MachineEventPublisher;
import com.forgewatch.machine_service.repository.MachineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer for machine management operations.
 * Handles business logic, validation, and event publishing.
 *
 * @author Md. Raihan Shikder (Raihan-89)
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MachineService {

    private final MachineRepository machineRepository;

    private final MachineEventPublisher machineEventPublisher;

    @Transactional(readOnly = true)
    public MachineResponse getMachineByPublicId(String publicId) {
        Machine machine = machineRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Machine", "publicId", publicId));
        return mapToResponse(machine);
    }

    public MachineResponse registerMachine(MachineRequest request) {

        if (machineRepository.existsByMachineCode(request.getMachineCode())) {
            throw new DuplicateResourceException("Machine", "machineCode", request.getMachineCode());
        }

        Machine machine = Machine.builder()
                .machineCode(request.getMachineCode())
                .machineName(request.getMachineName())
                .department(request.getDepartment())
                .location(request.getLocation())
                .description(request.getDescription())
                .status(MachineStatus.IDLE)
                .build();

        Machine saved = machineRepository.save(machine);

        log.info("Machine registered: {} (publicId: {})", saved.getMachineCode(), saved.getPublicId());

        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<MachineResponse> getAllMachines() {
        return machineRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<MachineResponse> getAllMachinesPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return machineRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public MachineResponse getMachineById(Long id) {
        Machine machine = machineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Machine", "id", id));
        return mapToResponse(machine);
    }

    @Transactional(readOnly = true)
    public List<MachineResponse> getMachinesByDepartment(String department) {
        return machineRepository.findByDepartment(department)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MachineResponse> getMachinesByStatus(MachineStatus status) {
        return machineRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public MachineResponse updateStatus(Long id, MachineStatus status) {

        Machine machine = machineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Machine", "id", id));

        if (machine.getStatus() == status) {
            throw new InvalidOperationException(
                    "Machine is already in " + status + " status");
        }

        machine.setStatus(status);

        if (status == MachineStatus.MAINTENANCE) {
            machine.setLastMaintenanceDate(LocalDateTime.now());
        }

        Machine saved = machineRepository.save(machine);

        MachineResponse response = mapToResponse(saved);

        if (status == MachineStatus.BREAKDOWN) {
            log.warn("Machine BREAKDOWN detected: {} (publicId: {})",
                    machine.getMachineCode(), machine.getPublicId());
            machineEventPublisher.publishMachineEvent(response);
        }

        return response;
    }

    public void deleteMachine(Long id) {
        if (!machineRepository.existsById(id)) {
            throw new ResourceNotFoundException("Machine", "id", id);
        }
        machineRepository.deleteById(id);
        log.info("Machine deleted: id={}", id);
    }

    private MachineResponse mapToResponse(Machine machine) {
        return MachineResponse.builder()
                .id(machine.getId())
                .publicId(machine.getPublicId())
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
