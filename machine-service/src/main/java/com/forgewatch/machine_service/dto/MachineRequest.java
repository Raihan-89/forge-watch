package com.forgewatch.machine_service.dto;

/**
 * @author Md. Raihan Shikder (Raihan-89)
 */

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MachineRequest {

    @NotBlank
    private String machineCode;

    @NotBlank
    private String machineName;

    private String department;

    private String location;

    private String description;
}