package com.forgewatch.defect_service.dto;

import com.forgewatch.defect_service.enums.DefectSeverity;
import com.forgewatch.defect_service.enums.DefectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefectResponse {

    private Long id;

    private String publicId;

    private String machineCode;

    private String department;

    private String title;

    private String description;

    private DefectSeverity severity;

    private DefectStatus status;

    private String reportedByEmail;

    private String resolvedByEmail;

    private LocalDateTime reportedAt;

    private LocalDateTime resolvedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
