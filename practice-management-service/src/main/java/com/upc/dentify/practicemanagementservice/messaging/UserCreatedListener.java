package com.upc.dentify.practicemanagementservice.messaging;

import com.upc.dentify.practicemanagementservice.config.RabbitConfig;
import com.upc.dentify.practicemanagementservice.domain.model.events.UserCreatedEvent;
import com.upc.dentify.practicemanagementservice.domain.model.events.UserUpdatedEvent;
import com.upc.dentify.practicemanagementservice.domain.services.OdontologistCommandService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedListener {

    private final OdontologistCommandService odontologistCommandService;

    public UserCreatedListener(OdontologistCommandService odontologistCommandService) {
        this.odontologistCommandService = odontologistCommandService;
    }

    @RabbitListener(queues = RabbitConfig.USER_CREATED_QUEUE)
    public void handleUserCreated(UserCreatedEvent event) {odontologistCommandService.handle(event);}

    @RabbitListener(queues = RabbitConfig.USER_UPDATED_QUEUE)
    public void handleUserUpdated(UserUpdatedEvent event) {odontologistCommandService.handle(event);}
}