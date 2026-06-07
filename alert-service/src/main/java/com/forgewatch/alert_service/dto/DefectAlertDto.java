package com.forgewatch.alert_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefectAlertDto {

    private Long id;

    private String machineCode;

    private String department;

    private String title;

    private String description;

    private String severity;

    private String status;

    private String reportedByEmail;

    private LocalDateTime reportedAt;
}