package com.forgewatch.machine_service.config;

/**
 * @author Md. Raihan Shikder (Raihan-89)
 */

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String MACHINE_QUEUE = "machine.queue";

    public static final String MACHINE_EXCHANGE = "machine.exchange";

    public static final String MACHINE_ROUTING_KEY = "machine.routing.key";

    @Bean
    public Queue machineQueue() {
        return new Queue(MACHINE_QUEUE, true);
    }

    @Bean
    public DirectExchange machineExchange() {
        return new DirectExchange(MACHINE_EXCHANGE);
    }

    @Bean
    public Binding machineBinding(Queue machineQueue,
                                  DirectExchange machineExchange) {
        return BindingBuilder
                .bind(machineQueue)
                .to(machineExchange)
                .with(MACHINE_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}