package com.forgewatch.shift_service.controller;

import com.forgewatch.common.dto.ApiResponse;
import com.forgewatch.shift_service.dto.ShiftRequest;
import com.forgewatch.shift_service.dto.ShiftResponse;
import com.forgewatch.shift_service.service.ShiftService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for shift management operations.
 *
 * @author Md. Raihan Shikder (Raihan-89)
 */
@RestController
@RequestMapping("/api/shifts")
@RequiredArgsConstructor
@Tag(name = "Shift Management", description = "Endpoints for managing production shifts")
public class ShiftController {

    private final ShiftService shiftService;

    @PostMapping
    @Operation(summary = "Create a new shift", description = "Creates a new production shift with worker assignments and targets")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Shift created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<ApiResponse<ShiftResponse>> createShift(
            @Valid @RequestBody ShiftRequest request) {
        ShiftResponse response = shiftService.createShift(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Shift created successfully"));
    }

    @GetMapping
    @Operation(summary = "Get all shifts", description = "Retrieves all scheduled shifts")
    public ResponseEntity<ApiResponse<List<ShiftResponse>>> getAllShifts() {
        return ResponseEntity.ok(ApiResponse.success(shiftService.getAllShifts()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get shift by ID", description = "Retrieves a specific shift by its internal database ID")
    public ResponseEntity<ApiResponse<ShiftResponse>> getShiftById(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(shiftService.getShiftById(id)));
    }

    @GetMapping("/public/{publicId}")
    @Operation(summary = "Get shift by public ID", description = "Retrieves a specific shift by its public identifier (e.g., SFT-XXXXXXXX)")
    public ResponseEntity<ApiResponse<ShiftResponse>> getShiftByPublicId(
            @PathVariable String publicId) {
        return ResponseEntity.ok(ApiResponse.success(shiftService.getShiftByPublicId(publicId)));
    }

    @GetMapping("/department/{department}")
    @Operation(summary = "Get shifts by department", description = "Retrieves all shifts for a specific department")
    public ResponseEntity<ApiResponse<List<ShiftResponse>>> getShiftsByDepartment(
            @PathVariable String department) {
        return ResponseEntity.ok(ApiResponse.success(shiftService.getShiftsByDepartment(department)));
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Get shifts by date", description = "Retrieves all shifts scheduled for a specific date")
    public ResponseEntity<ApiResponse<List<ShiftResponse>>> getShiftsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ApiResponse.success(shiftService.getShiftsByDate(date)));
    }

    @GetMapping("/department/{department}/date/{date}")
    @Operation(summary = "Get shifts by department and date", description = "Retrieves shifts filtered by department and date")
    public ResponseEntity<ApiResponse<List<ShiftResponse>>> getShiftsByDepartmentAndDate(
            @PathVariable String department,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ApiResponse.success(
                shiftService.getShiftsByDepartmentAndDate(department, date)));
    }

    @PutMapping("/{id}/production")
    @Operation(summary = "Update production output", description = "Updates the actual production count for a shift")
    public ResponseEntity<ApiResponse<ShiftResponse>> updateActualProduction(
            @PathVariable Long id,
            @RequestParam Integer actualProduction) {
        return ResponseEntity.ok(ApiResponse.success(
                shiftService.updateActualProduction(id, actualProduction),
                "Production updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a shift", description = "Removes a shift from the system")
    public ResponseEntity<ApiResponse<Void>> deleteShift(@PathVariable Long id) {
        shiftService.deleteShift(id);
        return ResponseEntity.ok(ApiResponse.success("Shift deleted successfully"));
    }
}
