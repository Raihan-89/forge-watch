package com.forgewatch.defect_service.controller;

import com.forgewatch.defect_service.dto.DefectRequest;
import com.forgewatch.defect_service.dto.DefectResponse;
import com.forgewatch.defect_service.enums.DefectSeverity;
import com.forgewatch.defect_service.enums.DefectStatus;
import com.forgewatch.defect_service.service.DefectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/defects")
@RequiredArgsConstructor
public class DefectController {

    private final DefectService defectService;

    @PostMapping
    public ResponseEntity<DefectResponse> reportDefect(
            @Valid @RequestBody DefectRequest request,
            @RequestHeader("X-User-Email") String userEmail) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(defectService.reportDefect(request, userEmail));
    }

    @GetMapping
    public ResponseEntity<List<DefectResponse>> getAllDefects() {
        return ResponseEntity.ok(defectService.getAllDefects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DefectResponse> getDefectById(
            @PathVariable Long id) {
        return ResponseEntity.ok(defectService.getDefectById(id));
    }

    @GetMapping("/machine/{machineCode}")
    public ResponseEntity<List<DefectResponse>> getDefectsByMachine(
            @PathVariable String machineCode) {
        return ResponseEntity.ok(defectService.getDefectsByMachine(machineCode));
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<DefectResponse>> getDefectsByDepartment(
            @PathVariable String department) {
        return ResponseEntity.ok(defectService.getDefectsByDepartment(department));
    }

    @GetMapping("/severity/{severity}")
    public ResponseEntity<List<DefectResponse>> getDefectsBySeverity(
            @PathVariable DefectSeverity severity) {
        return ResponseEntity.ok(defectService.getDefectsBySeverity(severity));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<DefectResponse>> getDefectsByStatus(
            @PathVariable DefectStatus status) {
        return ResponseEntity.ok(defectService.getDefectsByStatus(status));
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<DefectResponse> resolveDefect(
            @PathVariable Long id,
            @RequestHeader("X-User-Email") String userEmail) {
        return ResponseEntity.ok(defectService.resolveDefect(id, userEmail));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDefect(@PathVariable Long id) {
        defectService.deleteDefect(id);
        return ResponseEntity.noContent().build();
    }
}