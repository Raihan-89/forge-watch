package com.forgewatch.alert_service.listener;

import com.forgewatch.alert_service.config.RabbitMQConfig;
import com.forgewatch.alert_service.dto.DefectAlertDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DefectEventListener {

    @RabbitListener(queues = RabbitMQConfig.DEFECT_QUEUE)
    public void handleDefectEvent(DefectAlertDto defect) {

        log.warn("=================================================");
        log.warn("HIGH SEVERITY DEFECT ALERT!");
        log.warn("Defect ID     : {}", defect.getId());
        log.warn("Machine Code  : {}", defect.getMachineCode());
        log.warn("Department    : {}", defect.getDepartment());
        log.warn("Title         : {}", defect.getTitle());
        log.warn("Severity      : {}", defect.getSeverity());
        log.warn("Reported By   : {}", defect.getReportedByEmail());
        log.warn("Time          : {}", defect.getReportedAt());
        log.warn("=================================================");

        sendDefectAlert(defect);
    }

    private void sendDefectAlert(DefectAlertDto defect) {
        log.info("Sending defect alert to supervisor...");
        log.info("Subject : {} Defect reported on machine {}",
                defect.getSeverity(),
                defect.getMachineCode());
        log.info("Body    : {} - {}",
                defect.getTitle(),
                defect.getDescription());
    }
}