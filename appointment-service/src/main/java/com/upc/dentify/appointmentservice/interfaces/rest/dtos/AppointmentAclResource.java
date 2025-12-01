package com.upc.dentify.appointmentservice.interfaces.rest.dtos;

public record AppointmentAclResource(
        Long id,
        Long patientId,
        Long odontologistId,
        String state
) {
}
