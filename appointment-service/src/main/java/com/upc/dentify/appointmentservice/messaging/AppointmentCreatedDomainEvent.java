package com.upc.dentify.appointmentservice.messaging;

import com.upc.dentify.appointmentservice.domain.events.AppointmentCreatedEvent;

public class AppointmentCreatedDomainEvent {
    private final AppointmentCreatedEvent payload;
    public AppointmentCreatedDomainEvent(AppointmentCreatedEvent payload) { this.payload = payload; }
    public AppointmentCreatedEvent getPayload() { return payload; }
}
