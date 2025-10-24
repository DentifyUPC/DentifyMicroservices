package com.upc.dentify.iam.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.amqp.core.AcknowledgeMode;



import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "user.events";
    public static final String DLX = "user.events.dlx";
    public static final String USER_CREATED_ROUTING_KEY = "user.created";
    public static final String USER_CREATED_QUEUE = "user.created";
    public static final String USER_CREATED_DLQ = "user.created.dlq";
    public static final String USER_UPDATED_ROUTING_KEY = "user.updated";
    public static final String USER_UPDATED_QUEUE = "user.updated";
    public static final String USER_UPDATED_DLQ = "user.updated.dlq";

    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(DLX, true, false);
    }

    @Bean
    public Queue userCreatedQueue() {
//        Map<String, Object> args = new HashMap<>();
//        // si falla, enviar a DLX
//        args.put("x-dead-letter-exchange", DLX);
//        args.put("x-dead-letter-routing-key", USER_CREATED_DLQ);
        // opcional: control de TTL, tamaño, etc.
        return new Queue(USER_CREATED_QUEUE, true, false, false);
    }

    @Bean
    public Queue userCreatedDlq() {
        return new Queue(USER_CREATED_DLQ, true);
    }

    @Bean
    public Binding bindingUserCreated() {
        return BindingBuilder.bind(userCreatedQueue())
                .to(userExchange())
                .with(USER_CREATED_ROUTING_KEY);
    }

    @Bean
    public Queue userUpdatedQueue() {
        return new Queue(USER_UPDATED_QUEUE, true, false, false);
    }

    @Bean
    public Queue userUpdatedDlq() {
        return new Queue(USER_UPDATED_DLQ, true);
    }

    @Bean
    public Binding bindingUserUpdated() {
        return BindingBuilder.bind(userUpdatedQueue())
                .to(userExchange())
                .with(USER_UPDATED_ROUTING_KEY);
    }

    @Bean
    public Binding bindingCreatedDlq() {
        return BindingBuilder.bind(userCreatedDlq())
                .to(deadLetterExchange())
                .with(USER_CREATED_DLQ);
    }

    @Bean
    public Binding bindingUpdatedDlq() {
        return BindingBuilder.bind(userUpdatedDlq())
                .to(deadLetterExchange())
                .with(USER_UPDATED_DLQ);
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
