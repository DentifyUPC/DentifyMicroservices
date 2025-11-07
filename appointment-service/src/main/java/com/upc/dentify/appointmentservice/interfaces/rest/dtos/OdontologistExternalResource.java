package com.upc.dentify.appointmentservice.interfaces.rest.dtos;

public record OdontologistExternalResource(
        Long id,
        String firstName,
        String lastName,
        String shiftName
) {
}
