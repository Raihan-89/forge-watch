package com.forgewatch.defect_service.config;

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

    public static final String DEFECT_QUEUE = "defect.queue";

    public static final String DEFECT_EXCHANGE = "defect.exchange";

    public static final String DEFECT_ROUTING_KEY = "defect.routing.key";

    @Bean
    public Queue defectQueue() {
        return new Queue(DEFECT_QUEUE, true);
    }

    @Bean
    public DirectExchange defectExchange() {
        return new DirectExchange(DEFECT_EXCHANGE);
    }

    @Bean
    public Binding defectBinding(Queue defectQueue,
                                 DirectExchange defectExchange) {
        return BindingBuilder
                .bind(defectQueue)
                .to(defectExchange)
                .with(DEFECT_ROUTING_KEY);
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