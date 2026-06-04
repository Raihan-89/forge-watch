package com.forgewatch.defect_service.messaging;

import com.forgewatch.defect_service.config.RabbitMQConfig;
import com.forgewatch.defect_service.dto.DefectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefectEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishDefectEvent(DefectResponse defect) {

        log.info("Publishing defect event for machine: {} severity: {}",
                defect.getMachineCode(),
                defect.getSeverity());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.DEFECT_EXCHANGE,
                RabbitMQConfig.DEFECT_ROUTING_KEY,
                defect
        );
    }
}