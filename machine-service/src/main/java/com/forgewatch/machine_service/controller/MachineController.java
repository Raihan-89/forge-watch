package com.forgewatch.machine_service.controller;

import com.forgewatch.common.dto.ApiResponse;
import com.forgewatch.common.dto.PagedResponse;
import com.forgewatch.machine_service.dto.MachineRequest;
import com.forgewatch.machine_service.dto.MachineResponse;
import com.forgewatch.machine_service.enums.MachineStatus;
import com.forgewatch.machine_service.service.MachineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for machine management operations.
 *
 * @author Md. Raihan Shikder (Raihan-89)
 */
@RestController
@RequestMapping("/api/machines")
@RequiredArgsConstructor
@Tag(name = "Machine Management", description = "Endpoints for managing factory machines")
public class MachineController {

    private final MachineService machineService;

    @PostMapping
    @Operation(summary = "Register a new machine", description = "Creates a new machine record in the system")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Machine created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Machine code already exists")
    })
    public ResponseEntity<ApiResponse<MachineResponse>> registerMachine(
            @Valid @RequestBody MachineRequest request) {
        MachineResponse response = machineService.registerMachine(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Machine registered successfully"));
    }

    @GetMapping
    @Operation(summary = "Get all machines", description = "Retrieves all machines")
    public ResponseEntity<ApiResponse<List<MachineResponse>>> getAllMachines() {
        return ResponseEntity.ok(ApiResponse.success(machineService.getAllMachines()));
    }

    @GetMapping("/paged")
    @Operation(summary = "Get machines with pagination", description = "Retrieves machines with pagination support")
    public ResponseEntity<ApiResponse<PagedResponse<MachineResponse>>> getAllMachinesPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<MachineResponse> machinePage = machineService.getAllMachinesPaged(page, size);
        PagedResponse<MachineResponse> pagedResponse = PagedResponse.of(
                machinePage.getContent(),
                machinePage.getNumber(),
                machinePage.getSize(),
                machinePage.getTotalElements(),
                machinePage.getTotalPages()
        );
        return ResponseEntity.ok(ApiResponse.success(pagedResponse));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get machine by ID", description = "Retrieves a specific machine by its internal database ID")
    public ResponseEntity<ApiResponse<MachineResponse>> getMachineById(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(machineService.getMachineById(id)));
    }

    @GetMapping("/public/{publicId}")
    @Operation(summary = "Get machine by public ID", description = "Retrieves a specific machine by its public identifier (e.g., MCH-XXXXXXXX)")
    public ResponseEntity<ApiResponse<MachineResponse>> getMachineByPublicId(
            @PathVariable String publicId) {
        return ResponseEntity.ok(ApiResponse.success(machineService.getMachineByPublicId(publicId)));
    }

    @GetMapping("/department/{department}")
    @Operation(summary = "Get machines by department", description = "Retrieves all machines in a specific department")
    public ResponseEntity<ApiResponse<List<MachineResponse>>> getMachinesByDepartment(
            @PathVariable String department) {
        return ResponseEntity.ok(ApiResponse.success(machineService.getMachinesByDepartment(department)));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get machines by status", description = "Retrieves all machines with a specific operational status")
    public ResponseEntity<ApiResponse<List<MachineResponse>>> getMachinesByStatus(
            @PathVariable MachineStatus status) {
        return ResponseEntity.ok(ApiResponse.success(machineService.getMachinesByStatus(status)));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update machine status", description = "Updates the operational status of a machine. Use BREAKDOWN to trigger alerts.")
    public ResponseEntity<ApiResponse<MachineResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam MachineStatus status) {
        return ResponseEntity.ok(ApiResponse.success(
                machineService.updateStatus(id, status),
                "Machine status updated to " + status));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a machine", description = "Removes a machine from the system")
    public ResponseEntity<ApiResponse<Void>> deleteMachine(@PathVariable Long id) {
        machineService.deleteMachine(id);
        return ResponseEntity.ok(ApiResponse.success("Machine deleted successfully"));
    }
}
