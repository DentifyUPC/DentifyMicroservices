package com.upc.dentify.appointmentservice.messaging;

import com.upc.dentify.appointmentservice.config.RabbitConfig;
import com.upc.dentify.appointmentservice.domain.events.AppointmentCreatedEvent;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class AppointmentCreatedEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public AppointmentCreatedEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onAppointmentCreated(AppointmentCreatedDomainEvent event) {
        AppointmentCreatedEvent payload = event.getPayload();
        // añade headers/metadata si quieres
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_APPOINTMENT,
                RabbitConfig.APPOINTMENT_CREATED_ROUTING_KEY,
                payload,
                message -> {
                    message.getMessageProperties().setHeader("correlationId", payload.getId());
                    // persistente
                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return message;
                });
    }
}
