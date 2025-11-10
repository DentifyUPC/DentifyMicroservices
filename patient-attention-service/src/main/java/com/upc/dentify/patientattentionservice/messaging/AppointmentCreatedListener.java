package com.upc.dentify.patientattentionservice.messaging;

import com.upc.dentify.patientattentionservice.config.RabbitConfig;
import com.upc.dentify.patientattentionservice.domain.model.events.AppointmentCreatedEvent;
import com.upc.dentify.patientattentionservice.domain.model.events.UserCreatedEvent;
import com.upc.dentify.patientattentionservice.domain.model.events.UserUpdatedEvent;
import com.upc.dentify.patientattentionservice.domain.services.ClinicalRecordEntriesCommandService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AppointmentCreatedListener {
    private final ClinicalRecordEntriesCommandService clinicalRecordEntriesCommandService;

    public AppointmentCreatedListener(ClinicalRecordEntriesCommandService clinicalRecordEntriesCommandService) {
        this.clinicalRecordEntriesCommandService = clinicalRecordEntriesCommandService;
    }

    @RabbitListener(queues = RabbitConfig.APPOINTMENT_CREATED_QUEUE)
    public void handleAppointmentCreated(AppointmentCreatedEvent event) {
        clinicalRecordEntriesCommandService.handle(event);
    }
}
