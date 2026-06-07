package com.forgewatch.alert_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MachineAlertDto {

    private Long id;

    private String machineCode;

    private String machineName;

    private String department;

    private String location;

    private String status;

    private LocalDateTime updatedAt;
}