package com.upc.dentify.paymentservice.interfaces.rest.dtos;

public record AppointmentExternalResource(
        Long id,
        Long patientId,
        Long odontologistId,
        String state
) {
}
