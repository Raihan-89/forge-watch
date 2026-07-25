package com.forgewatch.defect_service.entity;

import com.forgewatch.defect_service.enums.DefectSeverity;
import com.forgewatch.defect_service.enums.DefectStatus;
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
 * Represents a quality defect reported on a machine.
 * Tracks severity, status, and resolution workflow.
 *
 * @author Md. Raihan Shikder (Raihan-89)
 */
@Entity
@Table(name = "defects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Defect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String publicId;

    @Column(nullable = false)
    private String machineCode;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private DefectSeverity severity;

    @Enumerated(EnumType.STRING)
    private DefectStatus status;

    private String reportedByEmail;

    private String resolvedByEmail;

    private LocalDateTime reportedAt;

    private LocalDateTime resolvedAt;

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
            publicId = "DFT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
    }
}
