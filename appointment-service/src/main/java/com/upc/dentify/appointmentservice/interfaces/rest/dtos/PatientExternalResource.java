package com.upc.dentify.appointmentservice.interfaces.rest.dtos;

public record PatientExternalResource(
        Long id,
        String firstName,
        String lastName
) {
}
