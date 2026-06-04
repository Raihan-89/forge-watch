package com.forgewatch.shift_service.dto;

import com.forgewatch.shift_service.enums.ShiftType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Md. Raihan Shikder (Raihan-89)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftResponse {

    private Long id;

    private ShiftType shiftType;

    private String department;

    private LocalDate shiftDate;

    private String supervisorEmail;

    private List<String> workerEmails;

    private Integer productionTarget;

    private Integer actualProduction;

    private LocalDateTime createdAt;
}