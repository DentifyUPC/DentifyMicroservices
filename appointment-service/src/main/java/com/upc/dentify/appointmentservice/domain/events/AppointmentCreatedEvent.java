package com.upc.dentify.appointmentservice.domain.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCreatedEvent {
    private Long id;
    private Long odontologistId;
    private Long patientId;
    private Long clinicId;
    private Long serviceId;
}
