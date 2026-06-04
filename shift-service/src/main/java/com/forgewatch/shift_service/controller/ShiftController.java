package com.forgewatch.shift_service.controller;

import com.forgewatch.shift_service.dto.ShiftRequest;
import com.forgewatch.shift_service.dto.ShiftResponse;
import com.forgewatch.shift_service.service.ShiftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Md. Raihan Shikder (Raihan-89)
 */
@RestController
@RequestMapping("/api/shifts")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    @PostMapping
    public ResponseEntity<ShiftResponse> createShift(
            @Valid @RequestBody ShiftRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(shiftService.createShift(request));
    }

    @GetMapping
    public ResponseEntity<List<ShiftResponse>> getAllShifts() {
        return ResponseEntity.ok(shiftService.getAllShifts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShiftResponse> getShiftById(
            @PathVariable Long id) {
        return ResponseEntity.ok(shiftService.getShiftById(id));
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<ShiftResponse>> getShiftsByDepartment(
            @PathVariable String department) {
        return ResponseEntity.ok(shiftService.getShiftsByDepartment(department));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<ShiftResponse>> getShiftsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(shiftService.getShiftsByDate(date));
    }

    @GetMapping("/department/{department}/date/{date}")
    public ResponseEntity<List<ShiftResponse>> getShiftsByDepartmentAndDate(
            @PathVariable String department,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(
                shiftService.getShiftsByDepartmentAndDate(department, date));
    }

    @PutMapping("/{id}/production")
    public ResponseEntity<ShiftResponse> updateActualProduction(
            @PathVariable Long id,
            @RequestParam Integer actualProduction) {
        return ResponseEntity.ok(
                shiftService.updateActualProduction(id, actualProduction));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShift(@PathVariable Long id) {
        shiftService.deleteShift(id);
        return ResponseEntity.noContent().build();
    }
}