package com.forgewatch.defect_service.service;

import com.forgewatch.defect_service.dto.DefectRequest;
import com.forgewatch.defect_service.dto.DefectResponse;
import com.forgewatch.defect_service.entity.Defect;
import com.forgewatch.defect_service.enums.DefectSeverity;
import com.forgewatch.defect_service.enums.DefectStatus;
import com.forgewatch.defect_service.messaging.DefectEventPublisher;
import com.forgewatch.defect_service.repository.DefectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefectService {

    private final DefectRepository defectRepository;

    private final DefectEventPublisher defectEventPublisher;

    public DefectResponse reportDefect(DefectRequest request,
                                       String reportedByEmail) {

        Defect defect = Defect.builder()
                .machineCode(request.getMachineCode())
                .department(request.getDepartment())
                .title(request.getTitle())
                .description(request.getDescription())
                .severity(request.getSeverity())
                .status(DefectStatus.OPEN)
                .reportedByEmail(reportedByEmail)
                .reportedAt(LocalDateTime.now())
                .build();

        Defect saved = defectRepository.save(defect);

        log.info("Defect reported on machine: {} by: {}",
                saved.getMachineCode(),
                saved.getReportedByEmail());

        DefectResponse response = mapToResponse(saved);

        if (request.getSeverity() == DefectSeverity.HIGH
                || request.getSeverity() == DefectSeverity.CRITICAL) {
            defectEventPublisher.publishDefectEvent(response);
        }

        return response;
    }

    public List<DefectResponse> getAllDefects() {
        return defectRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public DefectResponse getDefectById(Long id) {
        return mapToResponse(defectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Defect not found")));
    }

    public List<DefectResponse> getDefectsByMachine(String machineCode) {
        return defectRepository.findByMachineCode(machineCode)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<DefectResponse> getDefectsByDepartment(String department) {
        return defectRepository.findByDepartment(department)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<DefectResponse> getDefectsBySeverity(DefectSeverity severity) {
        return defectRepository.findBySeverity(severity)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<DefectResponse> getDefectsByStatus(DefectStatus status) {
        return defectRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public DefectResponse resolveDefect(Long id, String resolvedByEmail) {

        Defect defect = defectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Defect not found"));

        defect.setStatus(DefectStatus.RESOLVED);
        defect.setResolvedByEmail(resolvedByEmail);
        defect.setResolvedAt(LocalDateTime.now());

        log.info("Defect {} resolved by: {}", id, resolvedByEmail);

        return mapToResponse(defectRepository.save(defect));
    }

    public void deleteDefect(Long id) {
        defectRepository.deleteById(id);
    }

    private DefectResponse mapToResponse(Defect defect) {
        return DefectResponse.builder()
                .id(defect.getId())
                .machineCode(defect.getMachineCode())
                .department(defect.getDepartment())
                .title(defect.getTitle())
                .description(defect.getDescription())
                .severity(defect.getSeverity())
                .status(defect.getStatus())
                .reportedByEmail(defect.getReportedByEmail())
                .resolvedByEmail(defect.getResolvedByEmail())
                .reportedAt(defect.getReportedAt())
                .resolvedAt(defect.getResolvedAt())
                .build();
    }
}