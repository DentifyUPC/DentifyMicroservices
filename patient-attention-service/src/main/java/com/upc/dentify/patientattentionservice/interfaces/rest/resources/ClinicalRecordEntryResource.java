package com.upc.dentify.patientattentionservice.interfaces.rest.resources;

public record ClinicalRecordEntryResource(
        Long id, String evolution, String observation,
        Long odontologistId, Long clinicalRecordId, Long appointmentId,
        Long prescriptionId
) {
}
