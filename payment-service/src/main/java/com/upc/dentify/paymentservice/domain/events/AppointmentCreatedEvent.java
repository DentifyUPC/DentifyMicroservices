package com.upc.dentify.paymentservice.domain.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCreatedEvent {
    private Long id;                   // appointmentId
    private Long odontologistId;
    private Long patientId;
}
