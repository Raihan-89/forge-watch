package com.forgewatch.defect_service.dto;

import com.forgewatch.defect_service.enums.DefectSeverity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DefectRequest {

    @NotBlank
    private String machineCode;

    @NotBlank
    private String department;

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private DefectSeverity severity;
}