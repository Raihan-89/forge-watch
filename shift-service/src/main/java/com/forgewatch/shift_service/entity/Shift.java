package com.forgewatch.shift_service.entity;

import com.forgewatch.shift_service.enums.ShiftType;
import jakarta.persistence.*;
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
@Entity
@Table(name = "shifts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ShiftType shiftType;

    private String department;

    private LocalDate shiftDate;

    private String supervisorEmail;

    @ElementCollection
    @CollectionTable(
            name = "shift_workers",
            joinColumns = @JoinColumn(name = "shift_id")
    )
    @Column(name = "worker_email")
    private List<String> workerEmails;

    private Integer productionTarget;

    private Integer actualProduction;

    private LocalDateTime createdAt;
}