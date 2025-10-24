package com.upc.dentify.patientattentionservice.messaging;

import com.upc.dentify.patientattentionservice.config.RabbitConfig;
import com.upc.dentify.patientattentionservice.domain.model.events.UserCreatedEvent;
import com.upc.dentify.patientattentionservice.domain.model.events.UserUpdatedEvent;
import com.upc.dentify.patientattentionservice.domain.services.PatientCommandService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedListener {

    private final PatientCommandService patientCommandService;

    public UserCreatedListener(PatientCommandService patientCommandService) {
        this.patientCommandService = patientCommandService;
    }

    @RabbitListener(queues = RabbitConfig.USER_CREATED_QUEUE)
    public void handleUserCreated(UserCreatedEvent event) {
        patientCommandService.handle(event);
    }

    @RabbitListener(queues = RabbitConfig.USER_UPDATED_QUEUE)
    public void handleUserUpdated(UserUpdatedEvent event) {patientCommandService.handle(event);}
}
