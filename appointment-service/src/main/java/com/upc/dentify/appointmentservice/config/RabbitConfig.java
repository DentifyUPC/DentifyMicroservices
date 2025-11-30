package com.upc.dentify.appointmentservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_APPOINTMENT = "appointment.events";
    public static final String DLX = "appointment.events.dlx";
    public static final String APPOINTMENT_CREATED_ROUTING_KEY = "appointment.created";
    public static final String APPOINTMENT_CREATED_QUEUE = "payment.appointment.created";
    public static final String APPOINTMENT_CREATED_DLQ = "appointment.created.dlq";

    @Bean
    public TopicExchange appointmentExchange() {
        return new TopicExchange(EXCHANGE_APPOINTMENT, true, false);
    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(DLX, true, false);
    }

    @Bean
    public Queue appointmentCreatedQueue() {
        return new Queue(APPOINTMENT_CREATED_QUEUE, true, false, false);
    }

    @Bean
    public Queue appointmentCreatedDlq() {
        return new Queue(APPOINTMENT_CREATED_DLQ, true);
    }

    @Bean
    public Binding bindingAppointmentCreated() {
        return BindingBuilder.bind(appointmentCreatedQueue())
                .to(appointmentExchange())
                .with(APPOINTMENT_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding bindingCreatedDlq() {
        return BindingBuilder.bind(appointmentCreatedDlq())
                .to(deadLetterExchange())
                .with(APPOINTMENT_CREATED_DLQ);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }

    // Listener factory (para poder customizar ackMode, converter, concurrencia)
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf,
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            Jackson2JsonMessageConverter converter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, cf);
        factory.setMessageConverter(converter);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO); // AUTO está bien para empezar
        return factory;
    }
}
