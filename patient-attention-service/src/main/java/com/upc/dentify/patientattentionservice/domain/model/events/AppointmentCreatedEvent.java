package com.upc.dentify.patientattentionservice.domain.model.events;

import lombok.Getter;

@Getter
public class AppointmentCreatedEvent {
    private final Long id;
    private final Long odontologistId;
    private final Long patientId;

    public AppointmentCreatedEvent(Long id, Long odontologistId, Long patientId) {
        this.id = id;
        this.odontologistId = odontologistId;
        this.patientId = patientId;
    }
}
