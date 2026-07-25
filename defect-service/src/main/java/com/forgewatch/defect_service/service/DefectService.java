package com.forgewatch.defect_service.service;

import com.forgewatch.common.exception.InvalidOperationException;
import com.forgewatch.common.exception.ResourceNotFoundException;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
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

        log.info("Defect reported on machine: {} by: {} (publicId: {})",
                saved.getMachineCode(), saved.getReportedByEmail(), saved.getPublicId());

        DefectResponse response = mapToResponse(saved);

        if (request.getSeverity() == DefectSeverity.HIGH
                || request.getSeverity() == DefectSeverity.CRITICAL) {
            log.warn("High severity defect detected: {} on machine: {}",
                    saved.getSeverity(), saved.getMachineCode());
            defectEventPublisher.publishDefectEvent(response);
        }

        return response;
    }

    @Transactional(readOnly = true)
    public List<DefectResponse> getAllDefects() {
        return defectRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DefectResponse getDefectById(Long id) {
        Defect defect = defectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Defect", "id", id));
        return mapToResponse(defect);
    }

    @Transactional(readOnly = true)
    public DefectResponse getDefectByPublicId(String publicId) {
        Defect defect = defectRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Defect", "publicId", publicId));
        return mapToResponse(defect);
    }

    @Transactional(readOnly = true)
    public List<DefectResponse> getDefectsByMachine(String machineCode) {
        return defectRepository.findByMachineCode(machineCode)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DefectResponse> getDefectsByDepartment(String department) {
        return defectRepository.findByDepartment(department)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DefectResponse> getDefectsBySeverity(DefectSeverity severity) {
        return defectRepository.findBySeverity(severity)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DefectResponse> getDefectsByStatus(DefectStatus status) {
        return defectRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public DefectResponse resolveDefect(Long id, String resolvedByEmail) {

        Defect defect = defectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Defect", "id", id));

        if (defect.getStatus() == DefectStatus.RESOLVED
                || defect.getStatus() == DefectStatus.CLOSED) {
            throw new InvalidOperationException(
                    "Defect is already " + defect.getStatus());
        }

        defect.setStatus(DefectStatus.RESOLVED);
        defect.setResolvedByEmail(resolvedByEmail);
        defect.setResolvedAt(LocalDateTime.now());

        Defect saved = defectRepository.save(defect);

        log.info("Defect {} resolved by: {}", saved.getPublicId(), resolvedByEmail);

        return mapToResponse(saved);
    }

    public void deleteDefect(Long id) {
        if (!defectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Defect", "id", id);
        }
        defectRepository.deleteById(id);
        log.info("Defect deleted: id={}", id);
    }

    private DefectResponse mapToResponse(Defect defect) {
        return DefectResponse.builder()
                .id(defect.getId())
                .publicId(defect.getPublicId())
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
                .createdAt(defect.getCreatedAt())
                .updatedAt(defect.getUpdatedAt())
                .build();
    }
}
