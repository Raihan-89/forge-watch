package com.forgewatch.defect_service.entity;

import com.forgewatch.defect_service.enums.DefectSeverity;
import com.forgewatch.defect_service.enums.DefectStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "defects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Defect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
}