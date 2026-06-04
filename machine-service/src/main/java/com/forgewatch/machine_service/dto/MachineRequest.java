package com.forgewatch.machine_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Md. Raihan Shikder (Raihan-89)
 */
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