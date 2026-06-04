package com.forgewatch.shift_service.dto;

import com.forgewatch.shift_service.enums.ShiftType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Md. Raihan Shikder (Raihan-89)
 */
@Data
public class ShiftRequest {

    @NotNull
    private ShiftType shiftType;

    @NotNull
    private String department;

    @NotNull
    private LocalDate shiftDate;

    private String supervisorEmail;

    private List<String> workerEmails;

    private Integer productionTarget;
}