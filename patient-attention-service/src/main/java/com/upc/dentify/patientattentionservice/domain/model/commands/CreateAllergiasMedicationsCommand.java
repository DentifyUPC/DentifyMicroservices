package com.upc.dentify.patientattentionservice.domain.model.commands;

public record CreateAllergiasMedicationsCommand(String medicationName, Long clinicalRecordId) {
    public CreateAllergiasMedicationsCommand {
        if (medicationName == null || medicationName.isEmpty()) {
            throw new IllegalArgumentException("Medication name cannot be empty");
        }

        if (clinicalRecordId == null || clinicalRecordId <= 0) {
            throw new IllegalArgumentException("Clinical record id must be greater than 0");
        }
    }
}
