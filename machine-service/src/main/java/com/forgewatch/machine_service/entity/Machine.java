package com.forgewatch.machine_service.entity;

/**
 * @author Md. Raihan Shikder (Raihan-89)
 */

import com.forgewatch.machine_service.enums.MachineStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "machines")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String machineCode;

    @Column(nullable = false)
    private String machineName;

    private String department;

    private String location;

    private String description;

    @Enumerated(EnumType.STRING)
    private MachineStatus status;

    private LocalDateTime lastMaintenanceDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
