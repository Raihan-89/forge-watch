package com.forgewatch.machine_service.entity;

import com.forgewatch.machine_service.enums.MachineStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a machine on the factory floor.
 * Tracks operational status, maintenance schedule, and location.
 *
 * @author Md. Raihan Shikder (Raihan-89)
 */
@Entity
@Table(name = "machines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String publicId;

    @Column(nullable = false, unique = true)
    private String machineCode;

    @Column(nullable = false)
    private String machineName;

    private String department;

    private String location;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private MachineStatus status;

    private LocalDateTime lastMaintenanceDate;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

    @PrePersist
    public void generatePublicId() {
        if (publicId == null || publicId.isBlank()) {
            publicId = "MCH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
    }
}
