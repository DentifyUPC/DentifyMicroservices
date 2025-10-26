package com.upc.dentify.practicemanagementservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "user.events";
    public static final String USER_CREATED_ROUTING_KEY = "user.created";
    public static final String USER_CREATED_QUEUE = "user.created";
    public static final String USER_UPDATED_ROUTING_KEY = "user.updated";
    public static final String USER_UPDATED_QUEUE = "user.updated";

    @Bean
    public Queue userCreatedQueue() {
//        Map<String, Object> args = new HashMap<>();
//        // si falla, enviar a DLX
//        args.put("x-dead-letter-exchange", DLX);
//        args.put("x-dead-letter-routing-key", USER_CREATED_DLQ);
        return new Queue(USER_CREATED_QUEUE, true);
    }

    @Bean
    public Binding bindingUserCreated(Queue userCreatedQueue) {
        return BindingBuilder.bind(userCreatedQueue)
                .to(new TopicExchange(EXCHANGE))
                .with(USER_CREATED_ROUTING_KEY);
    }

    @Bean
    public Queue userUpdatedQueue() {
        return new Queue(USER_UPDATED_QUEUE, true);
    }

    @Bean
    public Binding bindingUserUpdated(Queue userUpdatedQueue) {
        return BindingBuilder.bind(userUpdatedQueue)
                .to(new TopicExchange(EXCHANGE))
                .with(USER_UPDATED_ROUTING_KEY);
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

    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(RabbitConfig.EXCHANGE, true, false);
    }
}