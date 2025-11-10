package com.upc.dentify.patientattentionservice.interfaces.rest.resources;

public record CreatePrescriptionItemResource(String medicationName, String description,
                                             Long prescriptionId) {
}
