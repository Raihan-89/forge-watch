package com.forgewatch.defect_service.controller;

import com.forgewatch.common.dto.ApiResponse;
import com.forgewatch.defect_service.dto.DefectRequest;
import com.forgewatch.defect_service.dto.DefectResponse;
import com.forgewatch.defect_service.enums.DefectSeverity;
import com.forgewatch.defect_service.enums.DefectStatus;
import com.forgewatch.defect_service.service.DefectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/defects")
@RequiredArgsConstructor
@Tag(name = "Defect Management", description = "Endpoints for reporting and managing production defects")
public class DefectController {

    private final DefectService defectService;

    @PostMapping
    @Operation(summary = "Report a new defect", description = "Creates a new defect record. HIGH and CRITICAL severities trigger alert notifications.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Defect reported successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<ApiResponse<DefectResponse>> reportDefect(
            @Valid @RequestBody DefectRequest request,
            @RequestHeader("X-User-Email") String userEmail) {
        DefectResponse response = defectService.reportDefect(request, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Defect reported successfully"));
    }

    @GetMapping
    @Operation(summary = "Get all defects", description = "Retrieves all reported defects")
    public ResponseEntity<ApiResponse<List<DefectResponse>>> getAllDefects() {
        return ResponseEntity.ok(ApiResponse.success(defectService.getAllDefects()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get defect by ID", description = "Retrieves a specific defect by its internal database ID")
    public ResponseEntity<ApiResponse<DefectResponse>> getDefectById(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(defectService.getDefectById(id)));
    }

    @GetMapping("/public/{publicId}")
    @Operation(summary = "Get defect by public ID", description = "Retrieves a specific defect by its public identifier (e.g., DFT-XXXXXXXX)")
    public ResponseEntity<ApiResponse<DefectResponse>> getDefectByPublicId(
            @PathVariable String publicId) {
        return ResponseEntity.ok(ApiResponse.success(defectService.getDefectByPublicId(publicId)));
    }

    @GetMapping("/machine/{machineCode}")
    @Operation(summary = "Get defects by machine", description = "Retrieves all defects for a specific machine")
    public ResponseEntity<ApiResponse<List<DefectResponse>>> getDefectsByMachine(
            @PathVariable String machineCode) {
        return ResponseEntity.ok(ApiResponse.success(defectService.getDefectsByMachine(machineCode)));
    }

    @GetMapping("/department/{department}")
    @Operation(summary = "Get defects by department", description = "Retrieves all defects in a specific department")
    public ResponseEntity<ApiResponse<List<DefectResponse>>> getDefectsByDepartment(
            @PathVariable String department) {
        return ResponseEntity.ok(ApiResponse.success(defectService.getDefectsByDepartment(department)));
    }

    @GetMapping("/severity/{severity}")
    @Operation(summary = "Get defects by severity", description = "Retrieves all defects with a specific severity level")
    public ResponseEntity<ApiResponse<List<DefectResponse>>> getDefectsBySeverity(
            @PathVariable DefectSeverity severity) {
        return ResponseEntity.ok(ApiResponse.success(defectService.getDefectsBySeverity(severity)));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get defects by status", description = "Retrieves all defects with a specific status")
    public ResponseEntity<ApiResponse<List<DefectResponse>>> getDefectsByStatus(
            @PathVariable DefectStatus status) {
        return ResponseEntity.ok(ApiResponse.success(defectService.getDefectsByStatus(status)));
    }

    @PutMapping("/{id}/resolve")
    @Operation(summary = "Resolve a defect", description = "Marks a defect as resolved")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Defect resolved"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Defect already resolved/closed")
    })
    public ResponseEntity<ApiResponse<DefectResponse>> resolveDefect(
            @PathVariable Long id,
            @RequestHeader("X-User-Email") String userEmail) {
        return ResponseEntity.ok(ApiResponse.success(
                defectService.resolveDefect(id, userEmail),
                "Defect resolved successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a defect", description = "Removes a defect from the system")
    public ResponseEntity<ApiResponse<Void>> deleteDefect(@PathVariable Long id) {
        defectService.deleteDefect(id);
        return ResponseEntity.ok(ApiResponse.success("Defect deleted successfully"));
    }
}
