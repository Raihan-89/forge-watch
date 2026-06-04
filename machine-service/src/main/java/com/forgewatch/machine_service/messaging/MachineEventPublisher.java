package com.forgewatch.machine_service.messaging;

import com.forgewatch.machine_service.config.RabbitMQConfig;
import com.forgewatch.machine_service.dto.MachineResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Md. Raihan Shikder (Raihan-89)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MachineEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishMachineEvent(MachineResponse machine) {

        log.info("Publishing machine event for: {} status: {}",
                machine.getMachineCode(),
                machine.getStatus());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.MACHINE_EXCHANGE,
                RabbitMQConfig.MACHINE_ROUTING_KEY,
                machine
        );
    }
}