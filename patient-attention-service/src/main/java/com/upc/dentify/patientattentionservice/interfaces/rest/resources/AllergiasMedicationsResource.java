package com.upc.dentify.patientattentionservice.interfaces.rest.resources;

import java.time.LocalDateTime;

public record AllergiasMedicationsResource(
        Long id,
        String medicationName,
        Long clinicalRecordId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
