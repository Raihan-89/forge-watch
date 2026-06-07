package com.forgewatch.alert_service.listener;

import com.forgewatch.alert_service.config.RabbitMQConfig;
import com.forgewatch.alert_service.dto.MachineAlertDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MachineEventListener {

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

        sendMaintenanceAlert(machine);
    }

    private void sendMaintenanceAlert(MachineAlertDto machine) {
        log.info("Sending maintenance alert to supervisor...");
        log.info("Subject : URGENT - Machine {} is DOWN", machine.getMachineCode());
        log.info("Body    : Machine {} in {} requires immediate attention.",
                machine.getMachineName(),
                machine.getDepartment());
    }
}