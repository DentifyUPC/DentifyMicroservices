package com.upc.dentify.patientattentionservice.interfaces.rest.resources;

public record PrescriptionItemResource(Long id, String medicationName,
                                       String description, Long prescriptionId) {
}
