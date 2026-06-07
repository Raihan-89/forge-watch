package com.forgewatch.alert_service.listener;

import com.forgewatch.alert_service.config.RabbitMQConfig;
import com.forgewatch.alert_service.dto.DefectAlertDto;
import com.forgewatch.alert_service.service.EmailService;
import com.forgewatch.alert_service.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefectEventListener {

    private final EmailService emailService;

    private final SmsService smsService;

    @Value("${notification.supervisor.phone}")
    private String supervisorPhone;

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

        emailService.sendDefectAlertEmail(defect);

        smsService.sendDefectAlertSms(defect, supervisorPhone);
    }
}