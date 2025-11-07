package com.upc.dentify.practicemanagementservice.interfaces.rest.resources;

public record OdontologistExternalResource(
        Long id,
        String firstName,
        String lastName,
        String shiftName
) {
}
