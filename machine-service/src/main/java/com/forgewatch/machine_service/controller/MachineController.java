package com.forgewatch.machine_service.controller;

import com.forgewatch.machine_service.dto.MachineRequest;
import com.forgewatch.machine_service.dto.MachineResponse;
import com.forgewatch.machine_service.enums.MachineStatus;
import com.forgewatch.machine_service.service.MachineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author Md. Raihan Shikder (Raihan-89)
 */
@RestController
@RequestMapping("/api/machines")
@RequiredArgsConstructor
public class MachineController {

    private final MachineService machineService;

    @PostMapping
    public ResponseEntity<MachineResponse> registerMachine(
            @Valid @RequestBody MachineRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(machineService.registerMachine(request));
    }

    @GetMapping
    public ResponseEntity<List<MachineResponse>> getAllMachines() {
        return ResponseEntity.ok(machineService.getAllMachines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MachineResponse> getMachineById(
            @PathVariable Long id) {
        return ResponseEntity.ok(machineService.getMachineById(id));
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<MachineResponse>> getMachinesByDepartment(
            @PathVariable String department) {
        return ResponseEntity.ok(machineService.getMachinesByDepartment(department));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<MachineResponse>> getMachinesByStatus(
            @PathVariable MachineStatus status) {
        return ResponseEntity.ok(machineService.getMachinesByStatus(status));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<MachineResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam MachineStatus status) {
        return ResponseEntity.ok(machineService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMachine(@PathVariable Long id) {
        machineService.deleteMachine(id);
        return ResponseEntity.noContent().build();
    }
}