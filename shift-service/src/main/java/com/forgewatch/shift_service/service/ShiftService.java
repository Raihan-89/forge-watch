package com.forgewatch.shift_service.service;

import com.forgewatch.shift_service.dto.ShiftRequest;
import com.forgewatch.shift_service.dto.ShiftResponse;
import com.forgewatch.shift_service.entity.Shift;
import com.forgewatch.shift_service.repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Md. Raihan Shikder (Raihan-89)
 */
@Service
@RequiredArgsConstructor
@Slf4j
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
                .createdAt(LocalDateTime.now())
                .build();

        Shift saved = shiftRepository.save(shift);

        log.info("Shift created for department: {} date: {}",
                saved.getDepartment(),
                saved.getShiftDate());

        return mapToResponse(saved);
    }

    public List<ShiftResponse> getAllShifts() {
        return shiftRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ShiftResponse getShiftById(Long id) {
        return mapToResponse(shiftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shift not found")));
    }

    public List<ShiftResponse> getShiftsByDepartment(String department) {
        return shiftRepository.findByDepartment(department)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ShiftResponse> getShiftsByDate(LocalDate date) {
        return shiftRepository.findByShiftDate(date)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ShiftResponse> getShiftsByDepartmentAndDate(
            String department, LocalDate date) {
        return shiftRepository.findByDepartmentAndShiftDate(department, date)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ShiftResponse updateActualProduction(Long id, Integer actualProduction) {

        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        shift.setActualProduction(actualProduction);

        return mapToResponse(shiftRepository.save(shift));
    }

    public void deleteShift(Long id) {
        shiftRepository.deleteById(id);
    }

    private ShiftResponse mapToResponse(Shift shift) {
        return ShiftResponse.builder()
                .id(shift.getId())
                .shiftType(shift.getShiftType())
                .department(shift.getDepartment())
                .shiftDate(shift.getShiftDate())
                .supervisorEmail(shift.getSupervisorEmail())
                .workerEmails(shift.getWorkerEmails())
                .productionTarget(shift.getProductionTarget())
                .actualProduction(shift.getActualProduction())
                .createdAt(shift.getCreatedAt())
                .build();
    }
}