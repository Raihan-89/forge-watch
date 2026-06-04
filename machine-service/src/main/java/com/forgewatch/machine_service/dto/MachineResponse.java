package com.forgewatch.machine_service.dto;

/**
 * @author Md. Raihan Shikder (Raihan-89)
 */

import com.forgewatch.machine_service.enums.MachineStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MachineResponse {

    private Long id;

    private String machineCode;

    private String machineName;

    private String department;

    private String location;

    private String description;

    private MachineStatus status;

    private LocalDateTime lastMaintenanceDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
