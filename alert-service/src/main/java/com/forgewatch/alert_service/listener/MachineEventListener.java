package com.forgewatch.alert_service.listener;

import com.forgewatch.alert_service.config.RabbitMQConfig;
import com.forgewatch.alert_service.dto.MachineAlertDto;
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
public class MachineEventListener {

    private final EmailService emailService;

    private final SmsService smsService;

    @Value("${notification.supervisor.phone}")
    private String supervisorPhone;

    @RabbitListener(queues = RabbitMQConfig.MACHINE_QUEUE)
    public void handleMachineEvent(MachineAlertDto machine) {

        log.warn("=================================================");
        log.warn("MACHINE BREAKDOWN ALERT!");
        log.warn("Machine Code  : {}", machine.getMachineCode());
        log.warn("Machine Name  : {}", machine.getMachineName());
        log.warn("Department    : {}", machine.getDepartment());
        log.warn("Location      : {}", machine.getLocation());
        log.warn("Status        : {}", machine.getStatus());
        log.warn("Time          : {}", machine.getUpdatedAt());
        log.warn("=================================================");

        emailService.sendMachineBreakdownEmail(machine);

        smsService.sendMachineBreakdownSms(machine, supervisorPhone);
    }
}