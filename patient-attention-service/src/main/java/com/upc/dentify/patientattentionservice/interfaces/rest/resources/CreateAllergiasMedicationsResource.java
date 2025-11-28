package com.upc.dentify.patientattentionservice.interfaces.rest.resources;

public record CreateAllergiasMedicationsResource(
        String medicationName,
        Long clinicalRecordId
) {
}
