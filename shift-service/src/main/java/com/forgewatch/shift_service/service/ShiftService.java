package com.forgewatch.shift_service.service;

import com.forgewatch.common.exception.InvalidOperationException;
import com.forgewatch.common.exception.ResourceNotFoundException;
import com.forgewatch.shift_service.dto.ShiftRequest;
import com.forgewatch.shift_service.dto.ShiftResponse;
import com.forgewatch.shift_service.entity.Shift;
import com.forgewatch.shift_service.repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service layer for shift management operations.
 * Handles shift scheduling, worker assignments, and production tracking.
 *
 * @author Md. Raihan Shikder (Raihan-89)
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ShiftService {

    private final ShiftRepository shiftRepository;

    public ShiftResponse createShift(ShiftRequest request) {

        Shift shift = Shift.builder()
                .shiftType(request.getShiftType())
                .department(request.getDepartment())
                .shiftDate(request.getShiftDate())
                .supervisorEmail(request.getSupervisorEmail())
                .workerEmails(request.getWorkerEmails())
                .productionTarget(request.getProductionTarget())
                .actualProduction(0)
                .build();

        Shift saved = shiftRepository.save(shift);

        log.info("Shift created for department: {} date: {} (publicId: {})",
                saved.getDepartment(), saved.getShiftDate(), saved.getPublicId());

        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ShiftResponse> getAllShifts() {
        return shiftRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ShiftResponse getShiftById(Long id) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shift", "id", id));
        return mapToResponse(shift);
    }

    @Transactional(readOnly = true)
    public ShiftResponse getShiftByPublicId(String publicId) {
        Shift shift = shiftRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Shift", "publicId", publicId));
        return mapToResponse(shift);
    }

    @Transactional(readOnly = true)
    public List<ShiftResponse> getShiftsByDepartment(String department) {
        return shiftRepository.findByDepartment(department)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ShiftResponse> getShiftsByDate(LocalDate date) {
        return shiftRepository.findByShiftDate(date)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ShiftResponse> getShiftsByDepartmentAndDate(
            String department, LocalDate date) {
        return shiftRepository.findByDepartmentAndShiftDate(department, date)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ShiftResponse updateActualProduction(Long id, Integer actualProduction) {

        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shift", "id", id));

        if (actualProduction < 0) {
            throw new InvalidOperationException(
                    "Actual production cannot be negative");
        }

        shift.setActualProduction(actualProduction);

        Shift saved = shiftRepository.save(shift);

        log.info("Shift {} production updated: {} (target: {})",
                saved.getPublicId(), actualProduction, saved.getProductionTarget());

        return mapToResponse(saved);
    }

    public void deleteShift(Long id) {
        if (!shiftRepository.existsById(id)) {
            throw new ResourceNotFoundException("Shift", "id", id);
        }
        shiftRepository.deleteById(id);
        log.info("Shift deleted: id={}", id);
    }

    private ShiftResponse mapToResponse(Shift shift) {
        return ShiftResponse.builder()
                .id(shift.getId())
                .publicId(shift.getPublicId())
                .shiftType(shift.getShiftType())
                .department(shift.getDepartment())
                .shiftDate(shift.getShiftDate())
                .supervisorEmail(shift.getSupervisorEmail())
                .workerEmails(shift.getWorkerEmails())
                .productionTarget(shift.getProductionTarget())
                .actualProduction(shift.getActualProduction())
                .createdAt(shift.getCreatedAt())
                .updatedAt(shift.getUpdatedAt())
                .build();
    }
}
